package com.imap.game.AppRepository;

import androidx.room.TypeConverter;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.imap.game.Texture;

public class TilesetConverter {

    static class TilesetHolder {
        public Array<Texture> tileset;

        public TilesetHolder(Array<Texture> textures) {
            this.tileset = textures;
        }
    }

    @TypeConverter
    public static Array<Texture> fromJson(String string) {
        JsonValue v = new JsonReader().parse(string);
        Array<Texture> tileset = new Array<>();
        for(int i = 0; i < v.size; i++){
            tileset.add(new Texture(v.get(i).get("layer").asInt(), v.get(i).get("x").asInt(), v.get(i).get("y").asInt(), v.get(i).get("tileId").asInt()));
            System.out.println(v.get(i).get("layer").asInt()+" "+v.get(i).get("x").asInt()+" "+v.get(i).get("y").asInt()+" "+v.get(i).get("tileId").asInt());
        }
        return tileset;
    }

    @TypeConverter
    public static String fromTileset(Array<Texture> tileset) {
        TilesetHolder textureHolder = new TilesetHolder(tileset);
        Json json = new Json();
        json.setSerializer(TilesetHolder.class, new Json.Serializer<TilesetHolder>() {
            @Override
            public void write(Json json, TilesetHolder object, Class knownType) {
                json.writeArrayStart();
                for(Texture texture : object.tileset) {
                    json.writeValue(texture);
                }
                json.writeArrayEnd();
            }

            @Override
            public TilesetHolder read(Json json, JsonValue jsonData, Class type) {
                return null;
            }
        });
        return json.toJson(textureHolder);
    }
}
