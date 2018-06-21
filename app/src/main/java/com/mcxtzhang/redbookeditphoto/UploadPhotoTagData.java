package com.mcxtzhang.redbookeditphoto;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jimmy.chen@dianping.com on 15-8-6.
 */
public class UploadPhotoTagData implements Parcelable {

    public static final int TYPE_INTEREST = 1;
    public static final int TYPE_POI = 2;
    public int tagType;
    public int tagId;
    public boolean isRight = true;
    public double yPosition;
    public double xPosition;
    public String content;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(tagType);
        dest.writeInt(tagId);
        dest.writeInt(isRight ? 1 : 0);
        dest.writeDouble(yPosition);
        dest.writeDouble(xPosition);
        dest.writeString(content);
    }

    public static final Creator<UploadPhotoTagData> CREATOR = new Creator<UploadPhotoTagData>() {
        @Override
        public UploadPhotoTagData[] newArray(int size) {
            return new UploadPhotoTagData[size];
        }

        @Override
        public UploadPhotoTagData createFromParcel(Parcel in) {
            return new UploadPhotoTagData(in);
        }
    };

    public UploadPhotoTagData(int id, double x, double y) {
        this.tagId = id;
        this.xPosition = x;
        this.yPosition = y;
    }

    public UploadPhotoTagData(int id, int type, int x, int y) {
        this(id, x, y);
        this.tagType = type;
    }

    public UploadPhotoTagData(int id, double x, double y, boolean isRight) {
        this(id, x, y);
        this.isRight = isRight;
    }

    public UploadPhotoTagData(Parcel in) {

        tagType = in.readInt();
        tagId = in.readInt();
        isRight = in.readInt() == 1;
        yPosition = in.readDouble();
        xPosition = in.readDouble();
        content = in.readString();
    }

    public void setTagType(int type) {
        this.tagType = type;
    }

    public JSONObject toJSONObject() {

        JSONObject obj = new JSONObject();
        try {
            obj.put("isRight", isRight);
            obj.put("yPosition", yPosition);
            obj.put("xPosition", xPosition);
            obj.put("content", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
