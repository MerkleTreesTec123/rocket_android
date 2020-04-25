package com.ifenduo.lib_base.net;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class CustomGsonConverterFactory extends Converter.Factory {
    private final Gson gson;

    public static CustomGsonConverterFactory create() {
        return create(new Gson());
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static CustomGsonConverterFactory create(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        return new CustomGsonConverterFactory(gson);
    }

    private CustomGsonConverterFactory(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new CustomGsonResponseConverter<>(gson, adapter);
    }

    class CustomGsonResponseConverter<T> implements Converter<ResponseBody, T> {
        private static final String TAG = "GsonResponseConverter";
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        CustomGsonResponseConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException {
            String body = value.string();

            try {
                // 获取json中的code，对json进行预处理
                JSONObject json = new JSONObject(body);

                if (json.has("data")) {
                    json.put("return", json.get("data"));
                    json.remove("data");
                }

                if (!json.has("return") || json.isNull("return")) {
                    json.put("return", new JSONObject());
                }
                return adapter.fromJson(json.toString());
            } catch (Exception e) {
                if (e instanceof JsonSyntaxException) {
                    if (e.getMessage().contains("string but was")) {
                        try {
                            JSONObject json = new JSONObject(body);
                            if (!json.has("return") || json.isNull("return")) {
                                json.put("return", "");
                            }
                            return adapter.fromJson(json.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        } finally {
                            value.close();
                        }
                    } else if (e.getMessage().contains("BEGIN_ARRAY but was")) {
                        try {
                            JSONObject json = new JSONObject(body);
                            if (!json.has("return") || json.isNull("return")) {
                                json.put("return", new JSONArray());
                            }
                            return adapter.fromJson(json.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        } finally {
                            value.close();
                        }
                    } else if (e.getMessage().contains("NUMBER but was")) {
                        try {
                            JSONObject json = new JSONObject(body);
                            if (!json.has("return") || json.isNull("return")) {
                                json.put("return", 0);
                            }
                            return adapter.fromJson(json.toString());
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            throw new RuntimeException(e.getMessage());
                        } finally {
                            value.close();
                        }
                    } else {
                        value.close();
                        throw new RuntimeException(e.getMessage());
                    }
                }else if (!TextUtils.isEmpty(e.getMessage()) && e.getMessage().contains("JSONArray cannot be converted to JSONObject")) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        JSONArray jsonArray = new JSONArray(body);
                        jsonObject.put("code", 1);
                        jsonObject.put("msg", "ok");
                        jsonObject.put("return", jsonArray);
                        return adapter.fromJson(jsonObject.toString());
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        throw new RuntimeException(e1.getMessage());
                    } finally {
                        value.close();
                    }
                } else {
                    value.close();
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }
}
