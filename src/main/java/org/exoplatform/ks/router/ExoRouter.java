/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ks.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.exoplatform.ks.router.regex.ExoMatcher;
import org.exoplatform.ks.router.regex.ExoPattern;


/**
 * Created by The eXo Platform SAS Author : eXoPlatform thanhvc@exoplatform.com 
 * Apr, 23, 2012
 */
public class ExoRouter {
  /**
   * All the loaded routes.
   */
  public static List<BBCode> routes = new CopyOnWriteArrayList<BBCode>();

  public static void reset() {
    routes.clear();
  }

  /**
   * Add new route which loaded from route configuration file.
   * 
   * @param bbcodeSyntax /{pageID}/ForumService
   * @param bbcodeName the action which appends to patch after "ForumService"
   *          string.
   */
  public static void addRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput) {
    addRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, null);
  }

  /**
   * Add new route which loaded from route configuration file.
   * 
   * @param bbcodeSyntax /{pageID}/ForumService
   * @param bbcodeName /{pageID}/ForumService
   * @param params the action which appends to patch after "ForumService" string
   *          ex: /{pageID}/{ForumService|}/{action} =>/{pageID}/ForumService/{}
   */
  public static void addRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput, String params) {
    appendRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, params);
  }

  public static void appendRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput, String params) {
    int position = routes.size();
    routes.add(position, getRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, params));
  }

  public static BBCode getRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput, String params) {
    return getRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, params, null, 0);
  }

  public static BBCode getRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput) {
    return getRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, null, null, 0);
  }

  public static BBCode getRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput, String params, String sourceFile, int line) {
    BBCode bbcode = new BBCode();
    bbcode.bbCodeSyntax = bbcodeSyntax.replace("//", "/");
    bbcode.bbCodeTag = bbcodeName;
    bbcode.bbCodeOutput = bbCodeOutput;
    bbcode.routesFile = sourceFile;
    bbcode.routesFileLine = line;
    bbcode.addParams(params);
    bbcode.compute();
    return bbcode;
  }

  /**
   * Add a new route at the beginning of the route list
   */
  public static void prependRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput, String params) {
    routes.add(0, getRoute(bbcodeSyntax, bbcodeName, bbCodeOutput, params));
  }

  /**
   * Add a new route at the beginning of the route list
   */
  public static void prependRoute(String bbcodeSyntax, String bbcodeName, String bbCodeOutput) {
    routes.add(0, getRoute(bbcodeSyntax, bbcodeName, bbCodeOutput));
  }

  public static BBCode route(String message) {
    for (BBCode route : routes) {
      Map<String, String> args = route.matches(message);
      if (args != null) {
        route.localArgs = args;
        return route;
      }
    }

    return null;
  }
  
  public static String output(String message) {
    BBCode bbcode = ExoRouter.route(message);
    
    return String.format(bbcode.bbCodeOutput, bbcode.localArgs.values().toArray());
    
  }

  /**
   * Route class which contains path, BBCode tag & argument list.
   * 
   * @author thanhvc
   *
   */
  public static class BBCode {

    public String bbCodeSyntax;
    
    public String bbCodeOutput;

    public String bbCodeTag;

    List<String> actionArgs = new ArrayList<String>(3);

    ExoPattern pattern;

    public String routesFile;

    List<ParamArg> args = new ArrayList<ParamArg>(3);

    Map<String, String> staticArgs = new HashMap<String, String>(3);

    Map<String, String> localArgs = null;

    public int routesFileLine;

    static ExoPattern customRegexPattern =  ExoPattern.compile("\\{([a-zA-Z_][a-zA-Z_0-9]*)\\}");
    
    static ExoPattern argsPattern = ExoPattern.compile("\\{<([^>]+)>([a-zA-Z_0-9]+)\\}");
    
    static ExoPattern paramPattern = ExoPattern.compile("([a-zA-Z_0-9]+):'(.*)'");
    
    static ExoPattern bbCodeOpenRegexPattern =  ExoPattern.compile("\\[([a-zA-Z_][a-zA-Z_0-9]*)\\]");
    
    static ExoPattern bbCodeCloseRegexPattern =  ExoPattern.compile("\\[\\/([a-zA-Z_][a-zA-Z_0-9]*)\\]");

    public void compute() {
      String patternString = this.bbCodeSyntax;
      patternString = customRegexPattern.matcher(patternString).replaceAll("\\{<[^/]+>$1\\}");
      ExoMatcher matcher = argsPattern.matcher(patternString);
      while (matcher.find()) {
        ParamArg arg = new ParamArg();
        arg.name = matcher.group(2);
        arg.constraint = ExoPattern.compile(matcher.group(1));
        args.add(arg);
      }

      patternString = argsPattern.matcher(patternString).replaceAll("({$2}$1)");
      patternString = bbCodeOpenRegexPattern.matcher(patternString).replaceAll("\\\\[$1\\\\]");
      patternString = bbCodeCloseRegexPattern.matcher(patternString).replaceAll("\\\\[\\/$1\\\\]");
      this.pattern = ExoPattern.compile(patternString);
      
      for (ParamArg arg : args) {
        if (patternString.contains("{" + arg.name + "}")) {
          patternString = patternString.replace("{" + arg.name + "}", "({" + arg.name + "}" + arg.constraint.toString() + ")");
          actionArgs.add(arg.name);
        }
      }
      
    }

    public void addParams(String params) {
      if (params == null || params.length() < 1) {
        return;
      }
      params = params.substring(1, params.length() - 1);
      for (String param : params.split(",")) {
        ExoMatcher matcher = paramPattern.matcher(param);
        if (matcher.matches()) {
          staticArgs.put(matcher.group(1), matcher.group(2));
        } else {
          System.out.println("Ignoring %s (static params must be specified as key:'value',...)");
        }
      }
    }
    
    /**
     * Base on defined Pattern, when provided URI path, 
     * this method will extract all of parameters path value 
     * in given path which reflects in defined Pattern
     * 
     * Example: 
     * defined Pattern = "/{pageID}/topic/{topicID}"
     * invokes:: matches("1256/topic/topic544343");
     * result: Map<String, String> = {"pageID" -> "1256"}, {"topicID" -> "topic544343"}
     * 
     * @param path : given URI path
     * @return
     */
    public Map<String, String> matches(String path) {
      ExoMatcher matcher = pattern.matcher(path);
      if (matcher.matches()) {
        Map<String, String> localArgs = new HashMap<String, String>();
        for (ParamArg arg : args) {
          if (arg.defaultValue == null) {
            localArgs.put(arg.name, matcher.group(arg.name));
          }
        }
        return localArgs;
      }

      return null;
    }

    static class ParamArg {
      String name;

      ExoPattern constraint;

      String defaultValue;
    }
  }

}
