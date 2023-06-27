package com.apexon.compass.dashboard.testutil;

import com.google.gson.*;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class GsonUtil {

	private static final GsonBuilder gsonBuilder = new GsonBuilder()
		.registerTypeAdapter(ObjectId.class,
				(JsonSerializer<ObjectId>) (src, typeOfSrc, context) -> new JsonPrimitive(src.toHexString()))
		.registerTypeAdapter(ObjectId.class, (JsonDeserializer<ObjectId>) GsonUtil::deserialize);

	private static ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		if (json instanceof JsonObject) {
			JsonObject jo = (JsonObject) json;

			return new ObjectId(jo.get("timestamp").getAsInt(), jo.get("counter").getAsInt());
		}
		return new ObjectId(json.getAsString());
	}

	public static Gson getGson() {
		return gsonBuilder.create();
	}

}
