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

import java.util.Map;

import junit.framework.TestCase;

import org.exoplatform.ks.router.ExoRouter.BBCode;

/**
 * Created by The eXo Platform SAS
 * Author : thanh_vucong
 *          thanh_vucong@exoplatform.com
 * Sep 5, 2012  
 */
public abstract class ExoRouterBaseTest extends TestCase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    initBBCodeTags();
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    ExoRouter.reset();
  }
  
  private void initBBCodeTags() {
    //BBCode: b tag
    ExoRouter.prependRoute("[b]{param1}[/b]", "b", "<b>%s</b>");
    //BBCode: b tag
    ExoRouter.addRoute("[i]{param1}[/i]", "i", "<i>%s</i>");
    
    //BBCode: email tag
    ExoRouter.addRoute("[email]{param1}[/email]", "email", "<a href='mailto:%1$2s'>%1$2s</a>");
    
    //BBCode: email display tag
    ExoRouter.addRoute("[email src='{param1}']{param2}[/email]", "email.display", "<a href='mailto:%1$2s'>%2$2s</a>");
    
  }
  
  public void assertRouter(BBCode route, String actionName, Map<String, String> expectedArgs) {
    assertNotNull(route);
    assertEquals(actionName, route.bbCodeTag);
    for(Map.Entry<String , String> entry : expectedArgs.entrySet()) {
      assertEquals(entry.getValue(), route.localArgs.get(entry.getKey()));
    }
  }
  
}
