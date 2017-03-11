package com.scenario.nanohttp;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.util.ServerRunner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasiliy on 11.03.17.
 */
@Service
public class NanoServer extends NanoHTTPD implements ApplicationContextAware {

   ApplicationContext ap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ap = applicationContext;
        nanoServer = applicationContext.getBean(NanoServer.class);
    }

    @Autowired
    private ServiceBean serviceBean;

    NanoServer nanoServer;


    @PostConstruct
    private void startServer() {
     new Thread(new Runnable() {
         @Override
         public void run() {
             ServerRunner.executeInstance(nanoServer);
         }
     }).start();

    }


    public NanoServer() {
        super(8080);
    }

    @Override
    public Response serve(IHTTPSession session) {

        if(session.getMethod() == Method.GET){
            FileInputStream fin= null;
            try {
                fin = new FileInputStream("/home/vasiliy/IdeaProjects/edu/nanohttpd/src/main/resources/index.html");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            };
            return newChunkedResponse(Response.Status.OK,MIME_HTML,fin);
        }

        if(session.getMethod().equals(Method.POST)){
            Map<String, String> files = new HashMap<String, String>();
           Method method = session.getMethod();
           if (Method.PUT.equals(method) || Method.POST.equals(method)) {
               try {
                   session.parseBody(files);
               } catch (IOException ioe) {
                   return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
               } catch (ResponseException re) {
                   return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
               }
           }
        }


        return newFixedLengthResponse(serviceBean.toString());




        //        BufferedReader br = null;
//        StringBuilder sb = new StringBuilder();
//        try {
//
//            String curLine;
//            br = new BufferedReader(new FileReader("/home/vasiliy/IdeaProjects/edu/nanohttpd/src/main/resources/index.html"));
//            while ((curLine = br.readLine()) !=  null)
//                sb.append(curLine);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(session.getMethod());
//        System.out.println(session.getParms());
//         String s = sb.toString();



      /*  try {
            return  newChunkedResponse(Response.Status.OK,MIME_HTML,new FileInputStream("/home/vasiliy/IdeaProjects/edu/nanohttpd/src/main/resources/index.html"));
           // return  newChunkedResponse(Response.Status.OK,MIME_HTML, new BufferedInputStream( ));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
*/


//       if(session.getMethod() == Method.POST) {
//           Map<String, String> files = new HashMap<String, String>();
//           Method method = session.getMethod();
//           if (Method.PUT.equals(method) || Method.POST.equals(method)) {
//               try {
//                   session.parseBody(files);
//               } catch (IOException ioe) {
//                   return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
//               } catch (ResponseException re) {
//                   return newFixedLengthResponse(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
//               }
//           }
           // get the POST body
           //String postBody = session.getQueryParameterString();
          /* // or you can access the POST request's parameters
           String a = session.getParms().get("a");
           String b = session.getParms().get("b");
            System.out.println(serviceBean);
          return newFixedLengthResponse(String.valueOf(Integer.parseInt(a) + Integer.parseInt(b))); // Or postParameter.
            return newFixedLengthResponse(serviceBean.toString()); // Or postParameter.*/
      // }

    }
}
