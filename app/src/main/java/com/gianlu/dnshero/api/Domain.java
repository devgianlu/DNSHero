package com.gianlu.dnshero.api;


import androidx.annotation.NonNull;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Domain implements Serializable {
    public final String name;
    public final boolean apex;
    public final RootNameserver root;
    public final DNSRecords authoritative;
    public final DNSRecords resolver;
    public final ArrayList<Diagnostic> diagnostics;

    public Domain(@NonNull JSONObject obj) throws JSONException {
        name = obj.getString("name");
        apex = obj.getBoolean("apex");
        root = new RootNameserver(obj.getJSONObject("parent"));
        authoritative = new DNSRecords(obj.getJSONObject("authoritative"));
        resolver = new DNSRecords(obj.getJSONObject("resolver"));
        diagnostics = Diagnostic.list(obj.getJSONObject("diagnostics").getJSONArray("results"));
        // TODO: errors field missing
    }

    public enum DiagnosticStatus {
        PASSED,
        FAILED,
        SKIPPED,
        INFO;

        public static DiagnosticStatus parse(String status) {
            switch (status) {
                case "passed":
                    return PASSED;
                default:
                case "failed":
                    return FAILED;
                case "skipped":
                    return SKIPPED;
                case "info":
                    return INFO;
            }
        }
    }

    public static class NS implements Serializable {
        public final String source;
        public final ArrayList<String> nameservers;
        public final Glue glue;
        public final float rtt;
        public final DNSRecord<DNSRecord.AEntry> a;
        public final DNSRecord<DNSRecord.AEntry> aaaa;

        private NS(JSONObject obj) throws JSONException {
            source = obj.getString("source");
            nameservers = CommonUtils.toStringsList(obj.getJSONArray("name-servers"), true);
            glue = new Glue(obj.getJSONObject("glue"));
            rtt = Utils.parseMs(obj.getString("rtt"));
            a = new DNSRecord<>(obj.getJSONObject("a"), DNSRecord.AEntry.class);
            aaaa = new DNSRecord<>(obj.getJSONObject("aaaa"), DNSRecord.AEntry.class);
        }

        public static ArrayList<NS> list(JSONArray array) throws JSONException {
            ArrayList<NS> list = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++)
                list.add(new NS(array.getJSONObject(i)));
            return list;
        }
    }

    public static class Glue implements Serializable {
        public final ArrayList<Entry> v4;
        public final ArrayList<Entry> v6;

        Glue(JSONObject obj) throws JSONException {
            if (CommonUtils.isStupidNull(obj, "v4")) v4 = null;
            else v4 = Entry.list(obj.getJSONArray("v4"));

            if (CommonUtils.isStupidNull(obj, "v6")) v6 = null;
            else v6 = Entry.list(obj.getJSONArray("v6"));
        }


        public static class Entry implements Serializable {
            public final String address;
            public final String name;

            private Entry(JSONObject obj) throws JSONException {
                address = obj.getString("address");
                name = obj.getString("name");
            }

            static ArrayList<Glue.Entry> list(JSONArray array) throws JSONException {
                ArrayList<Glue.Entry> list = new ArrayList<>(array.length());
                for (int i = 0; i < array.length(); i++)
                    list.add(new Glue.Entry(array.getJSONObject(i)));
                return list;
            }
        }
    }

    public static class DNSRecords implements Serializable {
        public final ArrayList<NS> ns;
        public final SOAEntries soa;
        public final MXEntries mx;
        public final AEntries a;
        public final AEntries aaaa;
        public final TXTEntries txt;
        public final CNAMEEntries cname;

        DNSRecords(JSONObject obj) throws JSONException {
            ns = NS.list(obj.getJSONArray("ns"));
            soa = new SOAEntries(obj.getJSONArray("soa"));
            mx = new MXEntries(obj.getJSONArray("mx"));
            a = new AEntries(obj.getJSONArray("a"));
            aaaa = new AEntries(obj.getJSONArray("aaaa"));
            txt = new TXTEntries(obj.getJSONArray("txt"));
            cname = new CNAMEEntries(obj.getJSONArray("cname"));
        }
    }

    public static abstract class DNSRecordsArrayList<E extends DNSRecord.Entry> extends ArrayList<DNSRecord<E>> implements Serializable, Comparator<E> {
        DNSRecordsArrayList(JSONArray array, Class<E> entryClass) throws JSONException {
            makeList(array, entryClass);
        }

        private void makeList(JSONArray array, Class<E> entryClass) throws JSONException {
            for (int i = 0; i < array.length(); i++)
                add(new DNSRecord<>(array.getJSONObject(i), entryClass));
        }

        public List<DNSRecord<E>> listRecordsThatHas(E entry) {
            List<DNSRecord<E>> records = new ArrayList<>();
            for (DNSRecord<E> dns : this)
                if (dns.records.contains(entry))
                    records.add(dns);

            return records;
        }

        public boolean hasSomethingRelevant() {
            for (DNSRecord<E> dns : this)
                if (!dns.records.isEmpty()) return true;

            return false;
        }

        public List<E> createRelevantDataList() {
            List<E> list = new ArrayList<>();
            for (DNSRecord<E> dns : this)
                for (E entry : dns.records)
                    if (!list.contains(entry))
                        list.add(entry);

            Collections.sort(list, this);

            return list;
        }

        @Override
        public int compare(E e1, E e2) {
            return e1.name.compareToIgnoreCase(e2.name);
        }
    }

    public static class SOAEntries extends DNSRecordsArrayList<DNSRecord.SOAEntry> implements Serializable {
        SOAEntries(JSONArray array) throws JSONException {
            super(array, DNSRecord.SOAEntry.class);
        }
    }

    public static class MXEntries extends DNSRecordsArrayList<DNSRecord.MXEntry> implements Serializable {
        MXEntries(JSONArray array) throws JSONException {
            super(array, DNSRecord.MXEntry.class);
        }

        @Override
        public int compare(DNSRecord.MXEntry o1, DNSRecord.MXEntry o2) {
            return o1.preference - o2.preference;
        }
    }

    public static class AEntries extends DNSRecordsArrayList<DNSRecord.AEntry> implements Serializable {
        AEntries(JSONArray array) throws JSONException {
            super(array, DNSRecord.AEntry.class);
        }
    }

    public static class TXTEntries extends DNSRecordsArrayList<DNSRecord.TXTEntry> implements Serializable {
        TXTEntries(JSONArray array) throws JSONException {
            super(array, DNSRecord.TXTEntry.class);
        }
    }

    public static class CNAMEEntries extends DNSRecordsArrayList<DNSRecord.CNAMEEntry> implements Serializable {
        CNAMEEntries(JSONArray array) throws JSONException {
            super(array, DNSRecord.CNAMEEntry.class);
        }
    }

    public static class RootNameserver implements Serializable {
        public final String name;
        public final float rtt;
        public final ArrayList<String> nameservers;
        public final Glue glue;

        RootNameserver(@NonNull JSONObject obj) throws JSONException {
            name = obj.getString("source");
            glue = new Glue(obj.getJSONObject("glue"));
            rtt = Utils.parseMs(obj.getString("rtt"));

            JSONArray array = obj.getJSONArray("name-servers");
            nameservers = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++)
                nameservers.add(array.getJSONObject(i).getString("name"));
        }
    }

    public static class Diagnostic implements Serializable {
        public final String name;
        public final String description;
        public final DiagnosticStatus status;
        public final HashMap<String, Boolean> sources;
        public final String recommendation;

        private Diagnostic(JSONObject obj) throws JSONException {
            JSONObject definition = obj.getJSONObject("definition");
            name = definition.getString("name");
            description = definition.getString("description");
            status = DiagnosticStatus.parse(obj.getString("status"));
            recommendation = CommonUtils.optString(obj, "recommendation");

            JSONObject sourcesObj = obj.getJSONObject("sources");
            sources = new HashMap<>();
            Iterator<String> keys = sourcesObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                sources.put(key, sourcesObj.getBoolean(key));
            }
        }

        static ArrayList<Diagnostic> list(JSONArray array) throws JSONException {
            ArrayList<Diagnostic> list = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++)
                list.add(new Diagnostic(array.getJSONObject(i)));
            return list;
        }
    }
}
