package com.gianlu.dnshero.NetIO;


import android.support.annotation.NonNull;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.commonutils.Sorting.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
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

    public Domain(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        apex = obj.getBoolean("apex");
        root = new RootNameserver(obj.getJSONObject("parent"));
        authoritative = new DNSRecords(obj.getJSONObject("authoritative"));
        resolver = new DNSRecords(obj.getJSONObject("resolver"));
        diagnostics = CommonUtils.toTList(obj.getJSONObject("diagnostics").getJSONArray("results"), Diagnostic.class, this);

        System.out.println(obj); // TODO: errors missing
    }

    private static float parseMs(String str) {
        float value = Float.parseFloat(str.substring(0, str.length() - 2));
        if (str.endsWith("µs")) return value / 1000f; // microseconds
        else return value; // milliseconds
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

    public static final class NaturalOrderComparator implements Comparator<Diagnostic> {

        @Override
        public int compare(Diagnostic o1, Diagnostic o2) {
            return 0;
        }
    }

    public class NS implements Serializable {
        public final String source;
        public final ArrayList<String> nameservers;
        public final Glue glue;
        public final float rtt;
        public final DNSRecord<AEntry> a;
        public final DNSRecord<AEntry> aaaa;

        public NS(JSONObject obj) throws JSONException {
            source = obj.getString("source");
            nameservers = CommonUtils.toStringsList(obj.getJSONArray("name-servers"), true);
            glue = new Glue(obj.getJSONObject("glue"));
            rtt = parseMs(obj.getString("rtt"));
            a = new DNSRecord<>(obj.getJSONObject("a"), AEntry.class);
            aaaa = new DNSRecord<>(obj.getJSONObject("aaaa"), AEntry.class);
        }
    }

    public class CNAMEEntry extends GeneralRecordEntry implements Serializable {
        public final String target;

        public CNAMEEntry(JSONObject obj) throws JSONException {
            super(obj);

            target = obj.getString("target");
        }
    }

    public class DNSRecord<E extends GeneralRecordEntry> implements Serializable {
        public final String source;
        public final ArrayList<E> records;
        public final float rtt;

        public DNSRecord(JSONObject obj, Class<E> entryClass) throws JSONException {
            source = obj.getString("source");
            records = CommonUtils.toTList(obj.getJSONArray("records"), entryClass, Domain.this);
            rtt = parseMs(obj.getString("rtt"));
        }
    }

    public abstract class GeneralRecordEntry implements Serializable {
        public final String name;
        public final int ttl;

        public GeneralRecordEntry(JSONObject obj) throws JSONException {
            name = obj.getString("name");
            ttl = obj.getInt("ttl");
        }
    }

    public class TXTEntry extends GeneralRecordEntry implements Serializable {
        public final ArrayList<String> text;

        public TXTEntry(JSONObject obj) throws JSONException {
            super(obj);

            text = CommonUtils.toStringsList(obj.getJSONArray("text"), false);
        }
    }

    public class MXEntry extends GeneralRecordEntry implements Serializable {
        public final int preference;
        public final String exchange;

        public MXEntry(JSONObject obj) throws JSONException {
            super(obj);

            preference = obj.getInt("preference");
            exchange = obj.getString("exchange");
        }
    }

    public class SOAEntry extends GeneralRecordEntry implements Serializable {
        public final String mname;
        public final String rname;
        public final int serial;
        public final int refresh;
        public final int retry;
        public final int expire;
        public final int minimum_ttl;

        public SOAEntry(JSONObject obj) throws JSONException {
            super(obj);

            mname = obj.getString("mname");
            rname = obj.getString("rname");
            serial = obj.getInt("serial");
            refresh = obj.getInt("refresh");
            retry = obj.getInt("retry");
            expire = obj.getInt("expire");
            minimum_ttl = obj.getInt("minimum-ttl");
        }
    }

    public class AEntry extends GeneralRecordEntry implements Serializable {
        public final String address;

        public AEntry(JSONObject obj) throws JSONException {
            super(obj);
            address = obj.getString("address");
        }
    }

    public class DNSRecords implements Serializable {
        public final ArrayList<NS> ns;
        public final SOAEntries soa;
        public final MXEntries mx;
        public final AEntries a;
        public final AEntries aaaa;
        public final TXTEntries txt;
        public final CNAMEEntries cname;

        public DNSRecords(JSONObject obj) throws JSONException {
            ns = CommonUtils.toTList(obj.getJSONArray("ns"), NS.class, Domain.this);
            soa = new SOAEntries(obj.getJSONArray("soa"));
            mx = new MXEntries(obj.getJSONArray("mx"));
            a = new AEntries(obj.getJSONArray("a"));
            aaaa = new AEntries(obj.getJSONArray("aaaa"));
            txt = new TXTEntries(obj.getJSONArray("txt"));
            cname = new CNAMEEntries(obj.getJSONArray("cname"));
        }
    }

    public abstract class DNSRecordsArrayList<E extends GeneralRecordEntry, R extends DNSRecordsArrayList.RelevantData> extends ArrayList<DNSRecord<E>> implements Serializable {
        public DNSRecordsArrayList(JSONArray array, Class<E> entryClass) throws JSONException {
            makeList(array, entryClass);
        }

        private void makeList(JSONArray array, Class<E> entryClass) throws JSONException {
            for (int i = 0; i < array.length(); i++)
                add(new DNSRecord<>(array.getJSONObject(i), entryClass));
        }

        @NonNull
        public abstract List<R> createRelevantDataList();

        public abstract class RelevantData {
            public final String name;

            public RelevantData(String name) {
                this.name = name;
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }
        }
    }

    public class SOAEntries extends DNSRecordsArrayList<SOAEntry, SOAEntries.RelevantData> implements Serializable {

        public SOAEntries(JSONArray array) throws JSONException {
            super(array, SOAEntry.class);
        }

        @NonNull
        @Override
        public List<SOAEntries.RelevantData> createRelevantDataList() {
            return new ArrayList<>();
        }

        public abstract class RelevantData extends DNSRecordsArrayList.RelevantData {
            public RelevantData(String name) {
                super(name);
            }
        }
    }

    public class MXEntries extends DNSRecordsArrayList<MXEntry, MXEntries.RelevantData> implements Serializable {

        public MXEntries(JSONArray array) throws JSONException {
            super(array, MXEntry.class);
        }

        @NonNull
        @Override
        public List<MXEntries.RelevantData> createRelevantDataList() {
            List<RelevantData> list = new ArrayList<>();
            for (DNSRecord<MXEntry> dns : this) {
                for (MXEntry entry : dns.records) {
                    RelevantData data = new RelevantData(entry.exchange, entry.preference);
                    if (!list.contains(data)) list.add(data);
                }
            }

            return list;
        }

        public class RelevantData extends DNSRecordsArrayList.RelevantData {
            private final int preference;

            public RelevantData(String name, int preference) {
                super(name);
                this.preference = preference;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                RelevantData that = (RelevantData) o;
                return preference == that.preference && name.equals(that.name);
            }
        }
    }

    public class AEntries extends DNSRecordsArrayList<AEntry, AEntries.RelevantData> implements Serializable {

        public AEntries(JSONArray array) throws JSONException {
            super(array, AEntry.class);
        }

        @NonNull
        @Override
        public List<AEntries.RelevantData> createRelevantDataList() {
            return new ArrayList<>();
        }

        public abstract class RelevantData extends DNSRecordsArrayList.RelevantData {
            public RelevantData(String name) {
                super(name);
            }
        }
    }

    public class TXTEntries extends DNSRecordsArrayList<TXTEntry, TXTEntries.RelevantData> implements Serializable {

        public TXTEntries(JSONArray array) throws JSONException {
            super(array, TXTEntry.class);
        }

        @NonNull
        @Override
        public List<TXTEntries.RelevantData> createRelevantDataList() {
            return new ArrayList<>();
        }

        public abstract class RelevantData extends DNSRecordsArrayList.RelevantData {
            public RelevantData(String name) {
                super(name);
            }
        }
    }

    public class CNAMEEntries extends DNSRecordsArrayList<CNAMEEntry, CNAMEEntries.RelevantData> implements Serializable {

        public CNAMEEntries(JSONArray array) throws JSONException {
            super(array, CNAMEEntry.class);
        }

        @NonNull
        @Override
        public List<CNAMEEntries.RelevantData> createRelevantDataList() {
            return new ArrayList<>();
        }

        public abstract class RelevantData extends DNSRecordsArrayList.RelevantData {
            public RelevantData(String name) {
                super(name);
            }
        }
    }

    public class Glue implements Serializable {
        public final ArrayList<Entry> v4;
        public final ArrayList<Entry> v6;

        public Glue(JSONObject obj) throws JSONException {
            if (CommonUtils.isStupidNull(obj, "v4")) v4 = null;
            else v4 = CommonUtils.toTList(obj.getJSONArray("v4"), Entry.class, this);

            if (CommonUtils.isStupidNull(obj, "v6")) v6 = null;
            else v6 = CommonUtils.toTList(obj.getJSONArray("v6"), Entry.class, this);
        }

        public class Entry implements Serializable {
            public final String address;
            public final String name;

            public Entry(JSONObject obj) throws JSONException {
                address = obj.getString("address");
                name = obj.getString("name");
            }
        }
    }

    public class RootNameserver implements Serializable {
        public final String name;
        public final float rtt;
        public final ArrayList<String> nameservers;
        public final Glue glue;

        public RootNameserver(JSONObject obj) throws JSONException {
            name = obj.getString("source");
            nameservers = CommonUtils.toStringsList(obj.getJSONArray("name-servers"), true);
            glue = new Glue(obj.getJSONObject("glue"));
            rtt = parseMs(obj.getString("rtt"));
        }
    }

    public class Diagnostic implements Serializable, Filterable<DiagnosticStatus> {
        public final String name;
        public final String description;
        public final DiagnosticStatus status;
        public final HashMap<String, Boolean> sources;
        public final String recommendation;

        public Diagnostic(JSONObject obj) throws JSONException {
            JSONObject definition = obj.getJSONObject("definition");
            name = definition.getString("name");
            description = definition.getString("description");
            status = DiagnosticStatus.parse(obj.getString("status"));
            recommendation = obj.optString("recommendation", null);

            JSONObject sourcesObj = obj.getJSONObject("sources");
            sources = new HashMap<>();
            Iterator<String> keys = sourcesObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                sources.put(key, sourcesObj.getBoolean(key));
            }
        }

        @Override
        public DiagnosticStatus getFilterable() {
            return status;
        }
    }
}
