package org.exoplatform.ks.router;


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

/**
 * Created by The eXo Platform SAS
 * Author : eXoPlatform
 *          exo@exoplatform.com
 * Apr 23, 2012  
 */
public class ExoRouterTest extends ExoRouterBaseTest {
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();

  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testItalic() throws Exception {
    String got = ExoRouter.output("[i] italic[/i]");
    
    assertEquals("<i> italic</i>", got);
  }
  
  public void testBold() throws Exception {
    String got = ExoRouter.output("[b] bold[/b]");
    
    assertEquals("<b> bold</b>", got);
  }
  
  public void testEmail() throws Exception {
    String got = ExoRouter.output("[email]demo@example.com[/email]");
    
    assertEquals("<a href='mailto:demo@example.com'>demo@example.com</a>", got);
  }
  
  public void testEmailDisplay() throws Exception {
    String got = ExoRouter.output("[email src='demo@example.com']email to demo[/email]");
    
    assertEquals("<a href='mailto:demo@example.com'>email to demo</a>", got);
  }

}
