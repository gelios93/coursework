package com.imap.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Math.abs;


public class GameScreen extends ScreenAdapter implements InputProcessor, GestureDetector.GestureListener {


    private OrthographicCamera camera;
    private IsometricTiledMapRenderer render;
    private TiledMap map;
    private SpriteBatch batch;
    private boolean buildMode;
    private Array<BuildMaket> makets;
    private Array<Build> builds;
    private Map<String, Integer> names;
    private Vector2 select;
    private int currentX;
    private int currentY;

    public GameScreen(SpriteBatch batch, Array<BuildMaket> makets) {
        this.batch = batch;
        this.makets = makets;
    }

    @Override
    public void show() {
        builds = new Array<>();
        select = new Vector2();
        map = new TmxMapLoader().load("map.tmx");
        names = new HashMap<>();
        names.put("less", 0);
        System.out.println("maket.getName()");
//        for (BuildMaket maket: makets) {
//            System.out.println(maket.getName());
//        }
//        makets = new Array<>();
//        makets.add(new BuildMaket("less", new Vector3(2,2,2), new Array<Texture>()));
//        makets.get(names.get("less")).getTileset().add(
//                new Texture(2, 0, 0, 6),
//                new Texture(2, 1, 0, 10),
//                new Texture(2, 0, 1, 0),
//                new Texture(2, 1, 1, 8));
//        makets.get(names.get("less")).getTileset().add(
//                new Texture(3, 0, 0, 3),
//                new Texture(3, 1, 0, 7),
//                new Texture(3, 0, 1, 4),
//                new Texture(3, 1, 1, 5));
//        System.out.println(makets.get(0).getTileset().get(0).getTileId());
//        for (Texture texture : makets.get(names.get("less")).getTileset()) {
////            System.out.println(texture.getLayer());
////        }

        camera = new OrthographicCamera();
        camera.zoom = 1.5f;
        camera.position.set(4500, -1000, 100);

        buildMode = false;
        render = new IsometricTiledMapRenderer(map);
        prevCell = new TiledMapTileLayer.Cell();


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        render.setView(camera);

        batch.begin();

        if(isTouchDown){
            Sprite sprite = new Sprite(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("select.png")));
            sprite.setPosition(currentX, currentY-64);
            sprite.draw(batch);
        }

        camera.update();
        render.render();

        batch.end();

        cameraController(camera);

    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        map.dispose();
        render.dispose();
    }

    private void cameraController(OrthographicCamera camera){


        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -10, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-10, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(10, 0, 0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Z)){
            camera.zoom -= 0.05f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)){
            camera.zoom += 0.05f;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    boolean isTouchDown;

    private TiledMapTileLayer.Cell prevCell;
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        isTouchDown = true;
        Vector3 mousePos = new Vector3(screenX, screenY, 0);
        camera.unproject(mousePos);
        int x = (int) (mousePos.x/256 - mousePos.y/128 + 0.55f);
        int y = (int) (mousePos.x/256 + mousePos.y/128 - 0.6f);
        Build currentBuild = null;
        for (int i = 3; i > 1; i--){
            for (Build build: builds) {
                for (Coord coord: build.getCoords()){
                    if (coord.getY() == y && coord.getX() == x && coord.getLayer() == i) {
                        currentBuild = build;
                        System.out.println("build: "+coord.getX()+" "+coord.getY());
                        break;
                    }
                }
                if(currentBuild != null)
                    break;
            }
            if(currentBuild != null)
                break;
        }
        if(currentBuild != null) {
            System.out.println("build: " + currentBuild.getCoords().get(0).getX() + " " + currentBuild.getCoords().get(0).getY());
            currentX = (currentBuild.getCoords().get(0).getX() + currentBuild.getCoords().get(0).getY()) * 128;
            currentY = (- currentBuild.getCoords().get(0).getX() + currentBuild.getCoords().get(0).getY()) * 64;
            System.out.println(currentX + " " + currentY);
        }
        else
            System.out.println("lol");
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!buildMode){
            Vector3 mousePos = new Vector3(screenX, screenY, 0);
            camera.unproject(mousePos);
            TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(2);
            System.out.println(mousePos.x + " " + mousePos.y);
            int x = (int) (mousePos.x/256 - mousePos.y/128 + 0.55f);
            int y = (int) (mousePos.x/256 + mousePos.y/128 - 0.6f);
            TiledMapTileLayer.Cell currentCell;


//            if(layer.getCell(x+1, y-1) != null){
//                currentCell = layer.getCell(x+1, y-1);
//            }else{
//                currentCell = layer.getCell(x, y);
//            }
            isTouchDown = false;
//            Build currentBuild = null;
//            for (int i = 3; i > 1; i--){
//                for (Build build: builds) {
//                    for (Coord coord: build.getCoords()){
//                        if (coord.getY() == y && coord.getX() == x && coord.getLayer() == i) {
//                            currentBuild = build;
//                            System.out.println("build: "+coord.getX()+" "+coord.getY());
//                            break;
//                        }
//                    }
//                    if(currentBuild != null)
//                        break;
//                }
//                if(currentBuild != null)
//                    break;
//            }
//            if(currentBuild != null) {
//                System.out.println("build: " + currentBuild.getCoords().get(0).getX() + " " + currentBuild.getCoords().get(0).getY());
//                currentX = (currentBuild.getCoords().get(0).getX() + currentBuild.getCoords().get(0).getY()) * 256;
//                currentY = (currentBuild.getCoords().get(0).getX() - currentBuild.getCoords().get(0).getY()) * 128;
//            }
//            else
//                System.out.println("lol");


            boolean isAllowed = true;
            currentCell = layer.getCell(x, y);
            Build newBuild = new Build(makets.get(0), new Array<Coord>());
            for(int i = x; i < x+newBuild.getMaket().getSize().x; i++){
                for(int j = y; j < y+newBuild.getMaket().getSize().y; j++){
                    if(layer.getCell(i, j)!=null)
                        isAllowed = false;
                }
            }
            if(isAllowed){
                builds.add(newBuild);
                for (Texture texture: newBuild.getMaket().getTileset()) {
                    layer = (TiledMapTileLayer) map.getLayers().get(texture.getLayer());
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(map.getTileSets().getTile(texture.getTileId()));
                    if(texture.getLayer() == 3) {
                        layer.setCell(x - 1 + texture.getX(), y + 1 + texture.getY(), cell);
                        newBuild.getCoords().add(new Coord(texture.getLayer(), x - 1 + texture.getX(), y + 1 + texture.getY()));
                    }
                    else {
                        layer.setCell(x + texture.getX(), y + texture.getY(), cell);
                        newBuild.getCoords().add(new Coord(texture.getLayer(), x + texture.getX(), y + texture.getY()));
                    }
//                Coord c = builds.get(0).getCoords().get(n++);
//                System.out.println(c.getLayer()+" "+c.getX()+" "+c.getY());
//                currentCell.getTile().getId()

                }
            }


            Json json = new Json();
            json.setOutputType(JsonWriter.OutputType.json);
            json.setElementType(Build.class, "coords", Coord.class);
            json.setSerializer(Build.class, new Json.Serializer<Build>() {
                @Override
                public void write(Json json, Build object, Class knownType) {
                    json.writeObjectStart();
                    json.writeValue("maket", object.getMaket().getName());
                    json.writeValue("coords", object.getCoords());
                    json.writeObjectEnd();
                }

                @Override
                public Build read(Json json, JsonValue jsonData, Class type) {
                    return null;
                }
            });
//            final String s = json.toJson(builds.get(0));
//            System.out.println("value="+s);
            final String s = "";





            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    URL url = null;
                    HttpURLConnection connection = null;
                    String response = null;
                    try {
                        //Create connection
                        url = new URL("https://stats1.pnit.od.ua/build/get");
                        connection = (HttpsURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestMethod("POST");
                        connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

                        OutputStreamWriter wr = new OutputStreamWriter (connection.getOutputStream());
                        wr.write("value="+s);
                        wr.flush();
                        wr.close();
                        String line = "";
                        InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                        BufferedReader reader = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null)
                        {
                            sb.append(line).append("\n");
                        }
                        // Response from server after login process will be stored in response variable.
                        response = sb.toString();
                        // You can perform UI operations here
                        Json json1 = new Json();
//                        System.out.println(json1.prettyPrint(response));
                        isr.close();
                        reader.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            });
            thread.start();



            System.out.println(x + " " + y);
//            if(prevCell != null )
//                prevCell.setTile(tile);
//            if(currentCell != null) {
//                tile = currentCell.getTile();
//                currentCell.setTile(map.getTileSets().getTileSet("Ura").getTile(3));
//            }
//            if(layer.getCell(x+1, y-1) != null){
//                prevCell = layer.getCell(x+1, y-1);
//            }else{
//                prevCell = layer.getCell(x, y);
//            }

            prevCell = currentCell;
        }
        buildMode = false;
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float x = Gdx.input.getDeltaX();
        float y = Gdx.input.getDeltaY();
        camera.translate(-(x*camera.zoom),(y*camera.zoom));
        buildMode = true;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(initialDistance < distance && camera.zoom - abs(initialDistance - distance) / 10000 >= 0.2)
            camera.zoom -= abs(initialDistance - distance) / 10000;
        if(initialDistance > distance && camera.zoom <= 2)
            camera.zoom += abs(initialDistance-distance)/10000;
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
