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
public class ExoRouterReserveTest extends ExoRouterBaseTest {
  @Override
  protected void setUp() throws Exception {
    super.setUp();

  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testReserveForForumHome() throws Exception {
    String got = ExoRouter.output("[i] italic[/i]");
    
    assertEquals("<i> italic</i>", got);
  }
 
}
