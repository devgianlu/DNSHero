package com.gianlu.dnshero.api;

import android.os.Build;

import androidx.annotation.Keep;

import com.gianlu.commonutils.CommonUtils;
import com.gianlu.dnshero.Utils;

import org.json.JSONArray;
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
        rtt = Utils.parseMs(obj.getString("rtt"));

        try {
            records = Entry.list(obj.getJSONArray("records"), entryClass);
        } catch (ReflectiveOperationException ex) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) throw new JSONException(ex);
            else throw new JSONException(ex.getMessage());
        }
    }

    public static abstract class Entry implements Serializable {
        public final String name;
        public final int ttl;

        Entry(JSONObject obj) throws JSONException {
            name = obj.getString("name");
            ttl = obj.getInt("ttl");
        }

        static <E extends Entry> ArrayList<E> list(JSONArray array, Class<E> clazz) throws JSONException, ReflectiveOperationException {
            ArrayList<E> list = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++)
                list.add(clazz.getConstructor(JSONObject.class).newInstance(array.getJSONObject(i)));
            return list;
        }

        public abstract boolean equals(Object obj);
    }

    public static class SOAEntry extends Entry implements Serializable {
        public final String mname;
        public final String rname;
        public final int serial;
        public final int refresh;
        public final int retry;
        public final int expire;
        public final int minimum_ttl;

        @Keep
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

        public String getEmailAddress() {
            return rname.replaceFirst("\\.", "@");
        }

        @Override
        @SuppressWarnings("unchecked")
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

    public static class AEntry extends Entry implements Serializable {
        public final String address;

        @Keep
        public AEntry(JSONObject obj) throws JSONException {
            super(obj);
            address = obj.getString("address");
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AEntry aEntry = (AEntry) o;
            return address.equals(aEntry.address);
        }
    }

    public static class CNAMEEntry extends Entry implements Serializable {
        public final String target;

        @Keep
        public CNAMEEntry(JSONObject obj) throws JSONException {
            super(obj);

            target = obj.getString("target");
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CNAMEEntry that = (CNAMEEntry) o;
            return target.equals(that.target);
        }
    }

    public static class TXTEntry extends Entry implements Serializable {
        public final ArrayList<String> text;

        @Keep
        public TXTEntry(JSONObject obj) throws JSONException {
            super(obj);

            text = CommonUtils.toStringsList(obj.getJSONArray("text"), false);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TXTEntry txtEntry = (TXTEntry) o;
            return text.equals(txtEntry.text);
        }
    }

    public static class MXEntry extends Entry implements Serializable {
        public final int preference;
        public final String exchange;

        @Keep
        public MXEntry(JSONObject obj) throws JSONException {
            super(obj);

            preference = obj.getInt("preference");
            exchange = obj.getString("exchange");
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MXEntry entry = (MXEntry) o;
            return preference == entry.preference && exchange.equals(entry.exchange);
        }
    }
}
