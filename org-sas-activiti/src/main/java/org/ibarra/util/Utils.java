package org.ibarra.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.GraphicInfo;
import org.apache.batik.ext.awt.geom.Polyline2D;
import org.ibarra.util.model.Grafico;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static String decodeBase64(String dato) {
        try {
            byte[] bytesDecode = Base64.getDecoder().decode(dato.getBytes());
            String decode = new String(bytesDecode);
            return decode;
        } catch (Exception e) {
            return null;
        }
    }

    public static String fileComplementProcess(String activityKey) {
        return "process/" + activityKey + ".bpmn";
    }

    public static String gsonTransform(Object data) {
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").disableHtmlEscaping();
        Gson gson = builder.create();
        return gson.toJson(data);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Collection l) {
        if (l == null) {
            return true;
        } else return l.size() == 0;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }


    /**
     * Completar cadena con ceros string.
     *
     * @param cadena   the cadena
     * @param longitud the longitud
     * @return the string
     */
    public static String completarCadenaConCeros(String cadena, Integer longitud) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        StringBuilder ceros = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            ceros.append("0");
        }
        int tamanio = cadena.length();
        ceros = new StringBuilder(ceros.substring(0, longitud - tamanio));
        cadena = ceros + cadena;
        return cadena;
    }

    public static Integer getAnio(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    /**
     * To json string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String toJson(Object obj) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithModifiers(Modifier.STATIC).setExclusionStrategies(new ExclusionStrategy() {
                private Class clazz;

                @Override
                public boolean shouldSkipField(FieldAttributes field) {
                    return field.getDeclaringClass().equals(clazz);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    this.clazz = clazz;
                    return false;
                }
            }).setPrettyPrinting();
            builder.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson2 = builder.create();
            return gson2.toJson(obj);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Generar Json.", e);
        }
        return null;
    }

    public static Object toObjectFromJson(Object json, Class clazz) {
        if (json == null) {
            return null;
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithModifiers(Modifier.STATIC).setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).setPrettyPrinting();
            Gson gson2 = builder.create();
            return gson2.fromJson(json.toString(), clazz);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Transformar json a objeto.", e);
            return null;
        }
    }

    public static Object toObjectFromJson(Object json, Class clazz, String datePatter) {
        if (json == null) {
            return null;
        }
        try {
            if (datePatter == null) {
                datePatter = "yyyy-MM-dd HH:mm:ss";
            }
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization().setDateFormat(datePatter).excludeFieldsWithModifiers(Modifier.STATIC).setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).setPrettyPrinting();
            Gson gson2 = builder.create();
            return gson2.fromJson(json.toString(), clazz);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Transformar json a objeto.", e);
            return null;
        }
    }

    public static String getUltimaPosicion(String key, String regex) {
        try {
            String[] sp = key.split(regex);
            return sp[sp.length - 1];
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Obtener ultima posición desde un string.", e);
            return null;
        }
    }

    public static Date sumarDias(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    /**
     * Le da formato a la fecha con el pattern que se le pasa como parametro
     *
     * @param pattern  Formato que se desea obtener.
     * @param fechaFin Fecha a dar formato.
     * @return Fecha con el formato esperado.
     */
    public static String dateFormatPattern(String pattern, Date fechaFin) {
        if (fechaFin == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static String armarRutaJasper(String rutaReporte, String nombre) {
        return rutaReporte + nombre + ".jasper";
    }

    public static String armarRutaSubReport(String rutaReporte, String nombre) {
        return rutaReporte + nombre;
    }

    public static Map<String, Object> getUrlsImagenesSolicitud(String rutaImagenes) {
        Map<String, Object> map = new HashMap<>();
        map.put("HEADER_URL", rutaImagenes + "cabeceraIB.png");
        map.put("HEADER_URL_2", rutaImagenes + "logo_header.png");
        map.put("BACKGROUND", rutaImagenes + "marca_agua.png");
        map.put("FOOTER_URL", rutaImagenes + "pie_solicitud.png");
        map.put("TLP", rutaImagenes);
        return map;
    }

    public static String toCamelCase(String nombre) {
        try {
            if (Utils.isEmptyString(nombre)) {
                return "";
            }
            String[] words = nombre.split("[\\W_]+");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (i == 0) {
                    word = word.isEmpty() ? word : word.toLowerCase();
                } else {
                    word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
                }
                builder.append(word);
            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return nombre;
        }
    }

    public static Grafico centerGraphic(double x, double y, double width, double height) {
        double rx = x + (width / 2);
        double ry = y + (height / 2);
        return new Grafico(rx, ry);
    }

    public static Integer calcularAngulo(Grafico centerXSource, Grafico centerXTarget) {

        Double producto = (centerXSource.getX() * centerXTarget.getX()) + (centerXSource.getY() * centerXTarget.getY());
        Double compDesde = Math.pow(centerXSource.getX(), 2) + Math.pow(centerXSource.getY(), 2);
        Double compHasta = Math.pow(centerXTarget.getX(), 2) + Math.pow(centerXTarget.getY(), 2);
        Double cosAngulo = producto / (Math.sqrt(compDesde) * Math.sqrt(compHasta));
        System.out.println("Arco coseno: " + cosAngulo);
        Double value = Math.acos(cosAngulo);
        value = value * 100;
        /*double yr = 0.0;
        yr = centerXTarget.getY() - centerXSource.getY();
        double xr = 0.0;
        xr = centerXTarget.getX() - centerXSource.getX();
        if (Math.abs((centerXSource.getX() - centerXTarget.getX())) < 50) {
            System.out.println("distancia  " + (centerXSource.getX() - centerXTarget.getX()));
            return 0;
        }
        // Double value = Double.valueOf(yr / xr);
        if (value.isInfinite()) {
            System.out.println("Es infinito");
            value = 0.00;
        }*/

        System.out.println("Angulo: " + value);
        return Math.abs(value.intValue());
    }

    public static boolean verficarIntercepcion(Grafico grafico, BpmnModel model) {
        boolean f = false;
        for (Map.Entry<String, List<GraphicInfo>> en : model.getFlowLocationMap().entrySet()) {
            Polyline2D polyline = new Polyline2D();
            for (GraphicInfo gi : en.getValue()) {
                polyline.addPoint(Float.parseFloat(gi.getX() + ""), Float.parseFloat(gi.getY() + ""));
            }
            f = polyline.intersects(grafico.getX(), grafico.getY(), grafico.getWidth(), grafico.getHeight());
            if (f) {
                grafico.setKeyIntersecta(en.getKey());
                grafico.setIntersecciones(en.getValue());
                grafico.setPoligono(polyline);
                break;
            }
        }
        return f;
    }

    public static Grafico calcularPuntoCercano(GraphicInfo rect1, GraphicInfo rect2) {

        Double closestX = 0.0;
        Double closestY = 0.0;
        Grafico.Direccion direccion = Grafico.Direccion.RECTO_HACIA_ABAJO;
        Grafico.Punto llegada = new Grafico.Punto(0.0, 0.0);
        // (Math.abs(rect1.getY() - rect2.getY()) <= 20)
        if (rect1.getX() == rect2.getX()) {
            direccion = Grafico.Direccion.RECTO_HACIA_ABAJO;
            if (rect1.getY() < rect2.getY()) {
                closestX = rect1.getX() + (rect1.getWidth() / 2);
                closestY = rect1.getY() + rect1.getHeight();
                llegada = new Grafico.Punto(rect2.getX() + (rect2.getWidth() / 2), rect2.getY());
            } else if (rect1.getY() > rect2.getY()) {
                direccion = Grafico.Direccion.RECTO_HACIA_ARRIBA;
                closestX = rect1.getX() + (rect1.getWidth() / 2);
                closestY = rect1.getY();
                llegada = new Grafico.Punto(rect2.getX() + (rect2.getWidth() / 2), (rect2.getY() + rect2.getHeight()));
            }
        } else if (rect1.getX() < rect2.getX()) {
            if ((Math.abs(rect1.getY() - rect2.getY()) <= 20)) {
                direccion = Grafico.Direccion.IZQUIERDA_DERECHA;
                closestX = rect1.getX() + rect1.getWidth();
                closestY = rect1.getY() + (rect1.getHeight() / 2);
                llegada = new Grafico.Punto(rect2.getX(), rect2.getY() + (rect2.getHeight() / 2));
            } else if (rect1.getY() < rect2.getY() && (Math.abs(rect1.getY() - rect2.getY()) > 20)) {
                direccion = Grafico.Direccion.HACIA_ABAJO_DERECHA;
                closestX = rect1.getX() + rect1.getWidth();
                closestY = rect1.getY() + (rect1.getHeight() / 2);
                llegada = new Grafico.Punto(rect2.getX(), rect2.getY() + (rect2.getHeight() / 2));
            } else if (rect1.getY() > rect2.getY()) {
                direccion = Grafico.Direccion.HACIA_ARRIBA_IZQUIERDA;
                closestX = rect1.getX();
                closestY = rect1.getY() + (rect1.getHeight() / 2);
                llegada = new Grafico.Punto(rect2.getX() + rect1.getWidth(), (rect2.getY() + (rect2.getHeight() / 2)));
            }
        } else {
            direccion = Grafico.Direccion.HACIA_ABAJO_IZQUIERDA;
            closestX = rect1.getX() + (rect1.getWidth() / 2);
            closestY = rect1.getY() + rect1.getHeight();
            llegada = new Grafico.Punto(rect2.getX() + (rect2.getWidth()) / 2, rect2.getY());
        }

        Grafico grafico = new Grafico(closestX, closestY);
        grafico.setDireccion(direccion);
        grafico.setPuntoLLegada(llegada);
        return grafico;
    }


    public static Double round(Double value, int places) {
        if (value == null) {
            return null;
        }
        try {
            if (places < 0) {
                places = 0;
            }
            BigDecimal a = new BigDecimal(value);
            BigDecimal roundOff = a.setScale(places, BigDecimal.ROUND_HALF_UP);
            return roundOff.doubleValue();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return value;
        }

    }

    public static Date convertirStringToDate(String fecha, String formato) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formato);
            return format.parse(fecha);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object toObjecttoHashMap(Object json) {
        if (json == null) {
            return null;
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Type typeOfHashMap = null;

            if (json instanceof Collection || json instanceof List || json instanceof ArrayList) {
                typeOfHashMap = new com.google.common.reflect.TypeToken<List<Map<String, Object>>>() {}.getType();
            } else {
                typeOfHashMap = new com.google.common.reflect.TypeToken<Map<String, Object>>() {}.getType();
            }

            builder.enableComplexMapKeySerialization()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                    .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes field) {
                            return false; // No se omiten campos
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false; // No se omiten clases
                        }
                    })
                    .registerTypeAdapter(Map.class, new JsonDeserializer<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            Map<String, Object> map = new HashMap<>();
                            JsonObject jsonObject = jsonElement.getAsJsonObject();
                            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                                // Si el valor es un objeto, realizar conversión recursiva
                                if (entry.getValue().isJsonObject()) {
                                    map.put(entry.getKey(), context.deserialize(entry.getValue(), Map.class));
                                } else {
                                    map.put(entry.getKey(), entry.getValue());
                                }
                            }
                            return map;
                        }
                    });

            Gson gson2 = builder.create();
            String js = gson2.toJson(json);
            return gson2.fromJson(js, typeOfHashMap);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Transformar json a objeto.", e);
            return null;
        }
    }


    public static Object toObjecttoHashMap(Object json, List<String> ignoreFields, List<Class> ignoreClass) {
        if (json == null) {
            return null;
        }
        try {
            if (ignoreFields == null) {
                ignoreFields = new ArrayList<>();
            }
            if (ignoreClass == null) {
                ignoreClass = new ArrayList<>();
            }
            GsonBuilder builder = new GsonBuilder();
            Class finalParentClazz = null;
            Type typeOfHashMap = null;
            if (json instanceof Collection || json instanceof List || json instanceof ArrayList) {
                List l = (List) json;
                if (Utils.isNotEmpty(l)) {
                    finalParentClazz = ((List) json).get(0).getClass();
                } else {
                    finalParentClazz = Object.class;
                }
                typeOfHashMap = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
            } else {
                typeOfHashMap = new TypeToken<Map<String, Object>>() {
                }.getType();
                finalParentClazz = json.getClass();
            }
            Class finalParentClazz1 = finalParentClazz;
            List<String> finalIgnoreFields = ignoreFields;
            List<Class> finalIgnoreClass = ignoreClass;
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithModifiers(Modifier.STATIC).setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).setExclusionStrategies(new ExclusionStrategy() {
                private Class clazz;

                @Override
                public boolean shouldSkipField(FieldAttributes field) {
                    if (field.getAnnotation(JsonIgnore.class) != null) {
                        return true;
                    }
                    if (finalIgnoreFields.contains(field.getName())) {
                        return true;
                    }
                    return field.getDeclaredClass().equals(finalParentClazz1);
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    this.clazz = clazz;
                    return finalIgnoreClass.contains(clazz);
                }
            }).registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
            Gson gson2 = builder.create();
            String js = gson2.toJson(json);
            Object fromJson = gson2.fromJson(js, typeOfHashMap);
            return fromJson;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Transformar json a objeto. Data: " + json, e);
            return null;
        }
    }

    public static boolean isServiceAvailable(String url, int timeout) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            return (responseCode >= 200 && responseCode < 400);
        } catch (Exception e) {
            return false;
        }
    }
}
