package student.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String zTemperature,zIcon,zCity,zWeatherType,zHumidity,zWind,zTemp_Max,zTemp_Min;
    private int zCondition;

    public static weatherData fromJson(JSONObject jsonObject)
    {

        try
        {
            weatherData weatherZ =new weatherData();
            weatherZ.zCity=jsonObject.getString("name");
            weatherZ.zWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            int humidity=jsonObject.getJSONObject("main").getInt("humidity");
            weatherZ.zHumidity=Integer.toString(humidity);
            weatherZ.zCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            double windResult=jsonObject.getJSONObject("wind").getDouble("speed")*60*60/1000;
            int roundedWindValue=(int)Math.rint(windResult);
            weatherZ.zWind=Integer.toString(roundedWindValue);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherZ.zTemperature=Integer.toString(roundedValue);
            weatherZ.zIcon=updateWeatherIcon(weatherZ.zCondition);
            double tempMaxResult=jsonObject.getJSONObject("main").getDouble("temp_max")-273.15;
            int roundedMaxValue=(int)Math.rint(tempMaxResult);
            weatherZ.zTemp_Max=Integer.toString(roundedMaxValue);
            double tempMinResult=jsonObject.getJSONObject("main").getDouble("temp_min")-273.15;
            int roundedMinValue=(int)Math.rint(tempMinResult);
            weatherZ.zTemp_Min=Integer.toString(roundedMinValue);

            return weatherZ;
        }


        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    private static String updateWeatherIcon(int condition)
    {
        if(condition>=200 && condition<=299)
        {
            return "thunderstrom";
        }
        else if(condition>=300 && condition<=499)
        {
            return "lightrain";
        }
        else if(condition>=500 && condition<=599)
        {
            return "rain";
        }
        else  if(condition>=600 && condition<=699)
        {
            return "cold";
        }
        else if(condition>=700 && condition<=799)
        {
            return "fog";
        }
        else if(condition==800)
        {
            return "sunny";
        }
        else if(condition>=801 && condition<=802)
        {
            return "cloudy";
        }
        else if(condition>=803 && condition<=804)
        {
            return "overcast";
        }

        return "nothing";


    }

    public String getzTemperature() {
        return zTemperature+"°C";
    }

    public String getzIcon() {
        return zIcon;
    }

    public String getzCity() {
        return zCity;
    }

    public String getzWeatherType() {
        return zWeatherType;
    }

    public String getzHumidity() {return "Humidity:"+zHumidity+"%";}

    public String getzWind() {return "Wind: "+zWind+" km/h";}

    public String getzTemp_Max() {return "H:"+zTemp_Max+"°";}

    public String getzTemp_Min() {return "L:"+zTemp_Min+"°";}

}
