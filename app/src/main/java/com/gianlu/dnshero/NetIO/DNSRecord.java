package com.gianlu.dnshero.NetIO;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class DNSRecord<E extends DNSRecord.Entry> implements Serializable {
    public final String source;
    public final ArrayList<E> records;
    public final float rtt;

    public DNSRecord(JSONObject obj, Class<E> entryClass) throws JSONException {
        source = obj.getString("source");
        records = CommonUtils.toTList(obj.getJSONArray("records"), entryClass, this);
        rtt = Utils.parseMs(obj.getString("rtt"));
    }

    public abstract class Entry implements Serializable {
        public final String name;
        public final int ttl;

        public Entry(JSONObject obj) throws JSONException {
            name = obj.getString("name");
            ttl = obj.getInt("ttl");
        }

        public DNSRecord<E> getParent() {
            return DNSRecord.this;
        }

        public abstract boolean equals(Object obj);
    }

    public class SOAEntry extends Entry implements Serializable {
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SOAEntry soaEntry = (SOAEntry) o;
            return serial == soaEntry.serial
                    && refresh == soaEntry.refresh
                    && retry == soaEntry.retry
                    && expire == soaEntry.expire
                    && minimum_ttl == soaEntry.minimum_ttl
                    && mname.equals(soaEntry.mname)
                    && rname.equals(soaEntry.rname);
        }
    }

    public class AEntry extends Entry implements Serializable {
        public final String address;

        public AEntry(JSONObject obj) throws JSONException {
            super(obj);
            address = obj.getString("address");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AEntry aEntry = (AEntry) o;
            return address.equals(aEntry.address);
        }
    }

    public class CNAMEEntry extends Entry implements Serializable {
        public final String target;

        public CNAMEEntry(JSONObject obj) throws JSONException {
            super(obj);

            target = obj.getString("target");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CNAMEEntry that = (CNAMEEntry) o;
            return target.equals(that.target);
        }
    }

    public class TXTEntry extends Entry implements Serializable {
        public final ArrayList<String> text;

        public TXTEntry(JSONObject obj) throws JSONException {
            super(obj);

            text = CommonUtils.toStringsList(obj.getJSONArray("text"), false);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TXTEntry txtEntry = (TXTEntry) o;
            return text.equals(txtEntry.text);
        }
    }

    public class MXEntry extends Entry implements Serializable {
        public final int preference;
        public final String exchange;

        public MXEntry(JSONObject obj) throws JSONException {
            super(obj);

            preference = obj.getInt("preference");
            exchange = obj.getString("exchange");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MXEntry entry = (MXEntry) o;
            return preference == entry.preference && exchange.equals(entry.exchange);
        }
    }
}
