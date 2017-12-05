package com.gianlu.dnshero.NetIO;


import com.gianlu.commonutils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Domain implements Serializable {
    public final String name;
    public final boolean apex;
    public final RootDNSServer root;
    public final DNSRecords authoritative;
    public final DNSRecords resolver;
    public final ArrayList<Diagnostic> diagnostics;

    public Domain(JSONObject obj) throws JSONException {
        name = obj.getString("name");
        apex = obj.getBoolean("apex");
        root = new RootDNSServer(obj.getJSONObject("parent"));
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

    private <E extends GeneralRecordEntry> ArrayList<DNSRecord<E>> toDNSRecordsList(JSONArray array, Class<E> entryClass) throws JSONException {
        ArrayList<DNSRecord<E>> items = new ArrayList<>();
        for (int i = 0; i < array.length(); i++)
            items.add(new DNSRecord<>(array.getJSONObject(i), entryClass));
        return items;
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
        public final ArrayList<DNSRecord<SOAEntry>> soa;
        public final ArrayList<DNSRecord<MXEntry>> mx;
        public final ArrayList<DNSRecord<AEntry>> a;
        public final ArrayList<DNSRecord<AEntry>> aaaa;
        public final ArrayList<DNSRecord<TXTEntry>> txt;
        public final ArrayList<DNSRecord<CNAMEEntry>> cname;

        public DNSRecords(JSONObject obj) throws JSONException {
            ns = CommonUtils.toTList(obj.getJSONArray("ns"), NS.class, Domain.this);
            soa = toDNSRecordsList(obj.getJSONArray("soa"), SOAEntry.class);
            mx = toDNSRecordsList(obj.getJSONArray("mx"), MXEntry.class);
            a = toDNSRecordsList(obj.getJSONArray("a"), AEntry.class);
            aaaa = toDNSRecordsList(obj.getJSONArray("aaaa"), AEntry.class);
            txt = toDNSRecordsList(obj.getJSONArray("txt"), TXTEntry.class);
            cname = toDNSRecordsList(obj.getJSONArray("cname"), CNAMEEntry.class);
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

    public class RootDNSServer implements Serializable {
        public final String name;
        public final float rtt;
        public final ArrayList<String> nameservers;
        public final Glue glue;

        public RootDNSServer(JSONObject obj) throws JSONException {
            name = obj.getString("source");
            nameservers = CommonUtils.toStringsList(obj.getJSONArray("name-servers"), true);
            glue = new Glue(obj.getJSONObject("glue"));
            rtt = parseMs(obj.getString("rtt"));
        }
    }

    public class Diagnostic implements Serializable {
        public final String name;
        public final String description;
        public final DiagnosticStatus status;
        public final HashMap<String, Boolean> sources;

        public Diagnostic(JSONObject obj) throws JSONException {
            JSONObject definition = obj.getJSONObject("definition");
            name = definition.getString("name");
            description = definition.getString("description");
            status = DiagnosticStatus.parse(obj.getString("status"));

            JSONObject sourcesObj = obj.getJSONObject("sources");
            sources = new HashMap<>();
            Iterator<String> keys = sourcesObj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                sources.put(key, sourcesObj.getBoolean(key));
            }
        }
    }
}
