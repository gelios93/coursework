package com.imap.game.AppRepository;

import androidx.room.TypeConverter;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class Vector3Converter {

    @TypeConverter
    public static Vector3 fromString(String string) {
        JsonValue value = new JsonReader().parse(string);
        return new Vector3(value.get("x").asInt(), value.get("y").asInt(), value.get("z").asInt());
    }

    @TypeConverter
    public static String fromVector3(Vector3 vector3) {
        Json json = new Json();
        return json.toJson(vector3);
    }
}
