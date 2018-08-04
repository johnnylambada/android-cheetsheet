package app.model;

import com.ryanharter.auto.value.moshi.MoshiAdapterFactory;
import com.squareup.moshi.JsonAdapter;

@MoshiAdapterFactory
public abstract class ModelAdapterFactory implements JsonAdapter.Factory {

    public static JsonAdapter.Factory create(){
        return new AutoValueMoshi_ModelAdapterFactory();
    }
}
