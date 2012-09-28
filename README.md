# eXo Forum BBCode rendering mechanism

## Abstract
  * Currently, we are handling with complexity rendering, and maybe we have a performance problem.
  More detail: Default BBCode tag to support in eXo Forum:

<table>
  <tr>
    <td>No</td>
    <td>BBCode Tag</td>
    <td>HTML</td>
  </tr>
  <tr>
    <td>1</td>
    <td>[i] my italic test [/i] <br> [I] my italic test [/I] <br></td>
    <td><i> my italic test </i></td>
  </tr>
  <tr>
    <td>2</td>
    <td>[b] my bold test [/b]</td>
    <td><b> my bold test </b></td>
  </tr>
  <tr>
    <td>3</td>
    <td>[quote] my bold test [/quote]</td>
    <td><quote>my bold test</quote></td>
  </tr>
  <tr>
    <td>4</td>
    <td>aa [code] my bold test [/code] bc</td>
    <td>aa<code>my bold test</code>bc</td>
  </tr>
  <tr>
    <td>5</td>
    <td>[color=red]Red Text[/color]</td>
    <td><span style='color:red'>Red Text</span></td>
  </tr>
  <tr>
    <td>6</td>
    <td>[size=15]Red Text[/size]</td>
    <td><span style='font-size:15px'>Red Text</span></td>
  </tr>
  <tr>
    <td>7</td>
    <td>[url]http://example.com[/url]</td>
    <td><a href='http://example.com'>http://example.com</a></td>
  </tr>
  <tr>
    <td>8</td>
    <td>[url]ftp://example.com/file-explorer[/url]</td>
    <td><a href='ftp://example.com/file-explorer'>ftp://example.com/file-explorer</a></td>
  </tr>
</table>


  * This component which uses to improve the way to parser BBCode Tags in Forum, 
  it also made more easier to understand and maintenance as well.

### Default build

Use this command to build project:

    mvn clean install

By default, it will run only unit tests.

## How to use this library

Sample code:
 * BBCode Rendering
 
	String bbCode = "[i] my italic test [/i]";
    DefaultBBCodeParser parser = newParser(bbCode);
    String got = parser.process();
    assertEquals("<i> my italic test </i>", got);
        
  



