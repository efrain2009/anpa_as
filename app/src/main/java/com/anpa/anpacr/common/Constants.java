package com.anpa.anpacr.common;


public class Constants {


    public static final String ERROR_ACTIVAR_GPS = "ERROR ACTIVAR GPS";
    public static final int PICK_LOCATION = 1;
    public static final int SET_GPS = 1;
    public static final String ERROR_NO_INTERNET = "Por favor verifica tu conexión a internet e intenta de nuevo...";

    public static final String LATITUD_COSTA_RICA = "9.748916999999999";
    public static final String LONGITUD_COSTA_RICA = "-83.75342799999999";

    public final static String ESPERA = "Espera un momento";
    public final static String MSJ_ERROR = "Ups! Perdimos el rastro de la información. Intenta más tarde.";
    public final static String MSJ_SUCCESS_TIP = "Su experiencia está en periodo de aprobación por ANPA";
    public final static String BTN_ACEPTAR = "Aceptar";
    public final static String MSJ_ERROR_LOGIN_FB = "Debe ingresar con su cuenta de Facebook";

    public final static String URL_FACEBOOK_ANPA = "http://www.facebook.com/ANPACR/";

    public final static String URL_FACEBOOK = "https://developers.facebook.com/"; //"https://www.facebook.com/ANPACR/";

    /*Table Perdidos*/
    //public final static String TABLE_PERDIDOS = "ANPA03_PERDIDOS";
    public final static String TABLE_PERDIDOS = "perdidos";
    public final static String NOM_MASCOTA = "ANPA03_NOM_MASCOTA";
    public final static String NOM_DUENO = "ANPA03_NOM_DUENO";
    public final static String FOTO_PERDIDO = "ANPA03_FOTO";
    public final static String TELEFONO_PERDIDO = "ANPA03_TELEFONO";
    public final static String DETALLE_PERDIDO = "ANPA03_DETALLE";
    public final static String ESPECIE_PERDIDO = "ANPA03_ESPECIE";
    public final static String RAZA_PERDIDO = "ANPA03_RAZA";
    public final static String LATITUD_PERDIDO = "ANPA03_LATITUD";
    public final static String LONGITUD_PERDIDO = "ANPA03_LONGITUDE";
    public final static String PROVINCIA_PERDIDO = "ANPA03_PROVINCIA";
    public final static String CANTON_PERDIDO = "ANPA03_CANTON";
    public final static String ESPERA_PERDIDO = "Rastreando perdidos...."; //"Olfateando perdidos....";
    public final static String HABILITADO_PERDIDO = "ANPA03_HABILITADO";
    public final static String MSJ_ERROR_PERDIDO = "Ups! Perdimos el rastro de las mascotas en adopción. Intenta más tarde.";//"Ups! Perdimos el rastro de las mascotas perdidas. Intenta más tarde.";
    public final static String USUARIO = "ANPA03_USUARIO";
    public final static String USUARIO_NOMBRE = "UsuarioApp";
    public final static String TITTLE_PERDIDO_FB = "Ha compartido una adopción en ANPACR: ";
    public final static String TITTLE_PERDIDO_NO_LIST = "¡No hay mascostas disponibles en adopción!";  // "¡Que suerte! No hay mascotas perdidas por el momento";

    /*Table Castraciones
    public final static String TABLE_CASTRACIONES = "ANPA04_EVENTO";*/
    public final static String TABLE_CASTRACIONES = "castraciones";
    public final static String NOMBRE_CASTRACION = "ANPA04_NOMBRE_EVENTO";
    public final static String HORARIO_INICIO_CASTRACION = "ANPA04_HORARIO_INICIO";
    public final static String DESCRIPCION_CASTRACION = "ANPA04_DESCRIPCION";
    public final static String DOCTOR_CASTRACION = "ANPA04_DOCTOR";
    public final static String MONTO_CASTRACION = "ANPA04_MONTO";
    public final static String DIRECCION_CASTRACION = "ANPA04_DIRECCION";
    public final static String ENCARGADO_CASTRACION = "ANPA04_ENCARGADO";
    public final static String TIPO_EVENTO_CASTRACION = "ANPA04_TIPO_EVENTO";
    public final static String LATITUD_CASTRACION = "ANPA04_LATITUD";
    public final static String HORARIO_FIN_CASTRACION = "ANPA04_HORARIO_FIN";
    public final static String LONGITUD_CASTRACION = "ANPA04_LONGITUD";
    public final static String HABILITADO_CASTRACION = "ANPA04_HABILITADO";
    public final static String ESPERA_CASTRACION = "Olfateando castraciones....";
    public final static String MSJ_ERROR_CASTRATION = "Ups! Perdimos el rastro de las castraciones. Intenta más tarde.";
    public final static String IMAGE_CASTRACION = "ANPA04_IMAGE";
    public final static String MUESTRA_MONTO_CASTRACION = "ANPA04_MUESTRA_MONTO";
    public final static String TITTLE_CASTRACION_FB = "Asistiré a una campaña de castración";


    /*Table Preguntas frecuentes*/
    public final static String TABLE_PREGUNTA_FREC = "preguntas";
    public final static String DESC_PREGUNTA = "ANPA05_PREGUNTA";
    public final static String ORDEN_PREGUNTA = "ANPA05_ORDEN";
    public final static String RESPESTA_PREGUNTA = "ANPA05_RESPUESTA";
    public final static String TIPO_PREGUNTA = "ANPA05_TIPO";
    public final static String HABILITADO_PREGUNTA = "ANPA05_HABILITADO";

    /*Table Noticias*/
    //public final static String TABLE_NOTICIA = "ANPA01_NOTICIAS";
    public final static String TABLE_NOTICIA = "noticias";
    public final static String TITULO_NOTICIA = "ANPA01_TITULO";
    public final static String CONTENIDO_NOTICIA = "ANPA01_CONTENIDO";
    public final static String IMAGEN_NOTICIA = "ANPA01_IMAGE";
    public final static String HABILITADO_NOTICIA = "ANPA01_HABILITADO";
    public final static String ESPERA_NOTICIAS = "Mordiendo noticias....";
    public final static String MSJ_ERROR_NOTICIA = "Ups! Perdimos el rastro de las noticias. Intenta más tarde.";

    /*Table Patrocionios*/
    public final static String TABLE_PATROCINIO = "patrocinios";
    public final static String ORDEN_PATROCINIO = "ANPA06_ORDEN";
    public final static String NOMBRE_PATROCINIO = "ANPA06_NOMBRE";
    public final static String DESCRIPCION_PATROCINIO = "ANPA06_DESCRIPCION";
    public final static String URL_PATROCINIO = "ANPA06_URL";
    public final static String IMAGEN_PATROCINIO = "ANPA06_IMAGEN";
    public final static String HABILITADO_PATROCINIO = "ANPA06_HABILITADO";

    /*Table Consejo*/
    public final static String TABLE_CONSEJO = "consejos";
    public final static String DESCR_CONSEJO = "ANPA02_CONSEJO";
    public final static String AUTOR_CONSEJO = "ANPA02_AUTOR";
    public final static String ESPECIE_CONSEJO = "ANPA02_ESPECIE";
    public final static String RAZA_CONSEJO = "ANPA02_RAZA";
    public final static String ESTRELLA1_CONSEJO = "ANPA02_1ESTRELLAS";
    public final static String ESTRELLA2_CONSEJO = "ANPA02_2ESTRELLAS";
    public final static String ESTRELLA3_CONSEJO = "ANPA02_3ESTRELLAS";
    public final static String ESTRELLA4_CONSEJO = "ANPA02_4ESTRELLAS";
    public final static String ESTRELLA5_CONSEJO = "ANPA02_5ESTRELLAS";
    public final static String VOTOS_CONSEJO = "ANPA02_TOTAL_VOTOS";
    public final static String ESTADO_CONSEJO = "ANPA02_ESTADO";
    public final static String HABILITADO_CONSEJO = "ANPA02_HABILITADO";
    public final static String USUARIO_CONSEJO = "ANPA02_USUARIO";
    public final static String ESPERA_CONSEJO = "Persiguiendo consejos....";
    public final static String MSJ_ERROR_CONSEJO = "Ups! Perdimos el rastro de las consejos. Intenta más tarde.";
    public final static String MSJ_SUCCESS_CALIFICATION_TIP = "Acabas de calificar una experiencia.\n\n Gracias por puntuar!";
    public final static String TITTLE_CONSEJO_FB = "Ha compartido una experiencia en ANPACR: ";

    /*Home Activity*/
    public final static String TITLE_DESCRIPTION_CONTACTUS = "Contáctanos";
    public final static String TITLE_DESCRIPTION_DONATION = "Donaciones";
    public final static String TITLE_DESCRIPTION_ADOPTION = "Centro de adopción";
    public final static String TITLE_DESCRIPTION_CASTRATION = "Castraciones";
    public final static String TITLE_DESCRIPTION_NEWS = "Noticias";
    public final static String TITLE_DESCRIPTION_TIPS = "Experiencias";

    /*Castration Activity*/
    public static final String TITLE_SUGGESTION = "Sugerencias";

    /*News Activity*/
    public static final String TITLE_LAST_NEWS = "Lo último";
    public static final String TITLE_FREQ_ANSWER = "Preguntas";
    public static final String TITLE_SPONSOR = "Agradecimientos";
    public final static String ID_OBJ_DETAIL_NEWS = "news_objects";
    public final static String ID_OBJ_DETAIL_LOST = "lost_objects";

    /*Tips Activity*/
    public final static String ID_OBJ_DETAIL_TIP = "tip_objects";
    public final static String RAZA_PRUEBA = "Raza prueba";


    /*News Activity*/
    public static final String TITLE_LAST_CASTRATION = "Próximamente";
    public final static String ID_OBJ_DETAIL_CASTRATION = "castration_objects";

    public final static String LOG_TAG = "LOG: ";

    /*Preg frecuentes Activity*/
    public final static String ID_OBJ_DETAIL_FREQ_ANSWER = "answer_objects";

    /*Preg frecuentes Activity*/
    public final static String TITLE_DESCRIPTION_LOST = "Perdidos";
    public final static String TITLE_DESCRIPTION_LOST_DETAIL = "Mascota perdida";
    public static final String TITLE_LAST_LOST = "Ultimamente";
    public static final String TITLE_LOST = "Adopciones";
    public final static String ID_OBJ_DETAIL_LIST = "lost_objects";
    public final static String CONSEJOS_PARA = "Consejos para: ";
    public final static String TITLE_CONFIRMATION = "Recibo";

    /*Contactenos*/
    public final static String TABLE_MISCELANEOS = "micelaneos";
    public final static String FACEBOOK_MISCELANEOS = "ANPA08_FACEBOOK";
    public final static String DIRECCION_MISCELANEOS = "ANPA08_DIRECCION";
    public final static String TELEFONO1_MISCELANEOS = "ANPA08_TELEFONO1";
    public final static String TELEFONO2_MISCELANEOS = "ANPA08_TELEFONO2";
    public final static String MAIL1_MISCELANEOS = "ANPA08_EMAIL1";
    public final static String MAIL2_MISCELANEOS = "ANPA08_EMAIL2";
    public final static String URL_MISCELANEOS = "ANPA08_URL";


    public static final String ANPA_PHONE_PRIM = "+50640002672";
    public static final String ANPA_PHONE_SEC = "+50622252722";

    public static final String ANPA_EMAIL_PRIM = "info@anpacostaRica.org";
    public static final String ANPA_EMAIL_SEC = "educa@anpacostaRica.org";

    public static final String ANPA_EMAIL_SUBJECT = "Consulta vía ANPA App";

    public static final String ANPA_FACEBOOK_LINK = "fb://page/101774234247";
    public static final String ANPA_FACEBOOK_PUBLICA = "https://www.facebook.com/ANPACR/";

    public static final String ANPA_FACEBOOK_LINK_BACKUP = "https://m.facebook.com/pages/ANPA-Costa-Rica/101774234247";

    public static final String ANPA_TWITTER_LINK = "twitter://user?user_id=88675906";
    public static final String ANPA_TWITTER_LINK_BACKUP = "https://mobile.twitter.com/ANPACR";

    public static final String[] RACES = {"1,Perro", "2,Gato", "3,Ave", "4,Pez", "5,Roedor"};

    public static final String[] PROVINCE = {"1,San José", "2,Alajuela", "3,Cartago", "4,Heredia", "5,Guanacaste", "6,Puntarenas", "7,Limón"};

    public static final String TIP_SUCCESS = "¡Gracias por calificar!";

    /* App42 */
    public static final String App42ApiKey = "7389dc177e03422884045c7ac9227db10be51606e6bddbca4939f9d8d9b5cbb4";
    public static final String App42ApiSecret = "454319ede4caf512ed8ef92628fbc3b838b76bbfee9e812dd3f6eab76d602c46";
    public static final String App42DBName = "ANPAPRD";

    /*upload Image*/
    public final static String FILE = "_files";
    public final static String URL_FILE = "url";
    public final static String NAME_FILE = "name";
    public final static String TYPE_FILE = "type";
    public final static String ID_FILE = "fileId";

    //Paypal
    public static String PAYPAL_PURCHASE_ITEM = "Donación para ANPA";

    public final static String MSG_IMAGE_UPLOAD_ERROR = "Ha ocurrido un error cargando la imagen: ";

    public final static String MSJ_ERROR_GPS_ACTIVADO = "Debe de contar con el servicio de GPS activado";

    //PUBLICACIONES EN FACEBOOK
    public static final String PUBLICA_PERDIDO = "Ha publicado una adopción";
    public static final String PUBLICA_CONSEJO = "Ha compartido una experiencia";

    //Tipo de cambio BCCR
    public static final String WS_BCCR_ERROR = "En este momento no hay comunicación con el BCCR.";
}