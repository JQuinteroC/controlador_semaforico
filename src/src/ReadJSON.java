package src;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadJSON {
    private ArrayList confsConexion = new ArrayList<JSONObject>();
    private ArrayList confsDesconexion = new ArrayList<JSONObject>();
    private ArrayList confsPlan = new ArrayList<JSONObject>();

    private JSONObject jsonObject;

    public void readFile(String path){
        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(new FileReader(path));
            jsonObject = (JSONObject)obj;
            JSONArray confs = (JSONArray)jsonObject.get("CONF");
            System.out.println("confs:");
            Iterator iterator = confs.iterator();
            while (iterator.hasNext()) {
                String aux = iterator.next().toString();
                if (aux.indexOf('C') != -1)
                    confsConexion.add(jsonObject.get(aux));
                else if (aux.indexOf('D') != -1)
                    confsDesconexion.add(jsonObject.get(aux));
                else
                    confsPlan.add(jsonObject.get(aux));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

    }

    public String getPrimerMensaje(){
        Iterator iterator = confsConexion.iterator();
        String numbers = "";
        while(iterator.hasNext()){
            JSONObject aux = (JSONObject) iterator.next();
            String t2 = (String)aux.get("t2");
            numbers += t2+"-";

        }
        numbers = numbers.substring(0, numbers.length()-1);
        System.out.println(numbers);
        return numbers;
    }

    public ArrayList<JSONArray> getRutina(ArrayList<JSONObject> confs){
        Iterator iterator = confs.iterator();
        ArrayList<JSONArray> arr = new ArrayList<JSONArray>();
        while (iterator.hasNext()){
            JSONObject aux = (JSONObject) iterator.next();
            JSONArray tiempos = (JSONArray) aux.get("tiempos");
            arr.add(tiempos);
        }
        //System.out.println(arr);
        return arr;
    }

    public ArrayList<JSONArray> getRutinaConexion(){
        return getRutina(confsConexion);

    }

    public ArrayList<JSONArray> getRutinaPlan(){
        return getRutina(confsPlan);
    }

    public ArrayList<JSONArray> getRutinaDesconexion(){
        return getRutina(confsDesconexion);
    }


}
