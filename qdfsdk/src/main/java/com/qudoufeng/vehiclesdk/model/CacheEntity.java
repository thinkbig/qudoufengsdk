package com.qudoufeng.vehiclesdk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by WanYang on 2019/12/17/017.
 * 用不到有效期的缓存，因此暂时不用
 * company:杭州湾心网络科技有限公司
 */

public class CacheEntity implements Parcelable {

    /**
     * 负责反序列化
     */
    public static final Creator<CacheEntity> CREATOR = new Creator<CacheEntity>() {
        /**
         * 从序列化对象中，获取原始的对象
         * @param source
         * @return
         */
        @Override
        public CacheEntity createFromParcel(Parcel source) {
            return new CacheEntity(source);
        }

        /**
         * 创建指定长度的原始对象数组
         * @param size
         * @return
         */
        @Override
        public CacheEntity[] newArray(int size) {
            return new CacheEntity[0];
        }
    };

    private String key;
    private String value;
    private Long date;          // 过期时间，0表示不过期

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public boolean isValid() {
        return 0 == date || date > System.currentTimeMillis();
    }

    public CacheEntity(String key, String value, Long date) {
        this.key = key;
        this.value = value;
        this.date = date;
    }

    private CacheEntity(Parcel source) {
        this.key = source.readString();
        this.value = source.readString();
        this.date = source.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
        dest.writeLong(date);
    }
}
