package org.sas.administrativo.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    public static Integer getAnio(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    public static String codigoVerificacion(Integer longitud) {
        Random aleatorio = new Random(System.currentTimeMillis());
        int rangoCodigo = 999999;
        return completarCadenaConCeros(String.valueOf(aleatorio.nextInt(rangoCodigo)), longitud + 1);
    }

    public static String completarCadenaConCeros(String cadena, Integer longitud) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String ceros = "";
        for (int i = 0; i < longitud; i++) {
            ceros = ceros + "0";
        }
        int tamanio = cadena.length();
        ceros = ceros.substring(0, longitud - tamanio);
        cadena = ceros + cadena;
        return cadena;
    }

    public static Integer getHour(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.HOUR);
    }

    public static Integer getMinute(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MINUTE);
    }

    public static Integer getSecond(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.SECOND);
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
        if (json.toString() == "[]") {
            return null;
        }

        try {
            GsonBuilder builder = new GsonBuilder();
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).setPrettyPrinting();
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

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
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
                finalParentClazz = ((List) json).get(0).getClass();
                typeOfHashMap = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
            } else {
                typeOfHashMap = new TypeToken<Map<String, Object>>() {
                }.getType();
                finalParentClazz = json.getClass();
            }
            System.out.println("-> " + finalParentClazz);
            Class finalParentClazz1 = finalParentClazz;
            List<String> finalIgnoreFields = ignoreFields;
            List<Class> finalIgnoreClass = ignoreClass;
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss").excludeFieldsWithModifiers(Modifier.STATIC).setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).setExclusionStrategies(new ExclusionStrategy() {
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
                    if (finalIgnoreClass.contains(clazz)) {
                        return true;
                    }
                    return false;
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

    public static Object toObjecttoHashMap(Object json) {
        if (json == null) {
            return null;
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Class finalParentClazz = null;
            Type typeOfHashMap = null;
            if (json instanceof Collection || json instanceof List || json instanceof ArrayList) {
                finalParentClazz = ((List) json).get(0).getClass();
                typeOfHashMap = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
            } else {
                typeOfHashMap = new TypeToken<Map<String, Object>>() {
                }.getType();
                finalParentClazz = json.getClass();
            }
            System.out.println("-> " + finalParentClazz);
            Class finalParentClazz1 = finalParentClazz;
            builder.enableComplexMapKeySerialization().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)
                    .registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).setExclusionStrategies(new ExclusionStrategy() {
                        private Class clazz;

                        @Override
                        public boolean shouldSkipField(FieldAttributes field) {
                            return field.getDeclaredClass().equals(finalParentClazz1);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            this.clazz = clazz;
                            return false;
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

    public static int calcularAnios(Date fecha) {
        int dia = getDia(fecha);
        int mes = getMes(fecha);
        int anio = getAnio(fecha);
        // Seteamos el dia mes y año
        Calendar inicio = Calendar.getInstance();
        inicio.set(anio, mes - 1, dia);

        // Dia actual
        Calendar actual = Calendar.getInstance();

        // Calcula la diferencia de años
        int diferencia = actual.get(Calendar.YEAR) - inicio.get(Calendar.YEAR);

        // Si la diferencia de años es 0, no hay que restar nada
        // Si el año del dia de la fecha que yo paso es mayor que la actual, resto uno
        if (diferencia != 0 && inicio.get(Calendar.DAY_OF_YEAR) > actual.get(Calendar.DAY_OF_YEAR)) {
            diferencia--;
        }

        return diferencia;

    }

    public static String dateFormatPattern(String pattern, Date fechaFin) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//            return sdf.format(fechaFin);
            return new SimpleDateFormat(pattern, new Locale("es", "ES")).format(fechaFin);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH);
    }

    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(String user, String pass) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient(user, pass));
        return clientHttpRequestFactory;
    }

    public static HttpClient httpClient(String user, String pass) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        HttpClient client1 = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        return client1;
    }

    public static String gsonTransform(Object data) {
        GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping();
        Gson gson = builder.create();
        return gson.toJson(data);
    }
}
