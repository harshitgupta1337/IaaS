<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>
  
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type">
  <title>User Home</title>

  
</head><body><big><big><big><big>Welcome ${username}</big></big></big></big>
<div style="text-align: right;"><a href="/">Logout</a></div>

<br>

<br>

<br>

<br>

<table style="text-align: left; width: 100%; height: 397px; margin-left: auto; margin-right: auto;" border="1" cellpadding="2" cellspacing="2">

  <tbody>
    <tr>
      <td colspan="1" rowspan="4" style="vertical-align: top; width: 80%;"> &lt;#list approvedVMs as
vm&gt;
      <table style="text-align: left; width: 100%;" border="1" cellpadding="2" cellspacing="2">
        <tbody>
          <tr>
            <td style="text-align: center; vertical-align: middle;">VM
Name<br>
            </td>
            <td style="text-align: center; vertical-align: middle;">Status<br>
            </td>
            <td style="text-align: center; vertical-align: middle;">Actions<br>
            </td>
          </tr>
          <tr>
            <td style="vertical-align: top;"><a href="/account/$%7Busername%7D/vmDetails/$%7Bvm%5B" name="" ]}="">${vm["name"]}</a><br>
            </td>
            <td style="vertical-align: top;">${vm["status"]}<br>
            </td>
            <td style="vertical-align: top; width: 40%;">
            <table style="text-align: left; width: 100%; height: 100%;" border="1" cellpadding="2" cellspacing="2">
              <tbody>
                <tr>
                  <td style="vertical-align: middle; width: 33%; text-align: center;">&lt;#if
vm["status"]="ready" &gt;
                  <form method="post" action="/account/${username}/bootVM/${vm[" ]}="" name=""><button value="Boot" name="Boot">Boot</button><br>
                  </form>
<!--#if--> <br>
                  </td>
                  <td style="vertical-align: middle; text-align: center;"> &lt;#if
vm["status"]="running"&gt;
                  <form method="post" action="/account/${username}/shutDownVM/${vm[" ]}="" name=""><button value="ShutDown" name="ShutDown">Shut Down</button><br>
                  </form>
<!--#if--> <br>
                  </td>
                  <td style="vertical-align: middle; width: 33%; text-align: center;">&lt;#if
vm["status"]="ready" &gt;
                  <form method="post" action="/account/${username}/deleteVM/${vm[" ]}="" name=""><button value="DeleteVM" name="DeleteVM">Delete</button><br>
                  </form>
<!--#if--> <br>
                  </td>
                  <td style="vertical-align: middle; width: 33%; text-align: center;">&lt;#if
vm["status"]="running" &gt;
                  <form method="post" action="/account/${username}/terminal/${vm[" ]}="" name=""><button value="terminal" name="terminal">Terminal</button><br>
                  </form>
<!--#if--> <br>
                  </td>
                  <td style="vertical-align: top;"><br>
                  </td>
                </tr>
              </tbody>
            </table>
            </td>
          </tr>
<!--#list-->
        </tbody>
      </table>
      <br>
      </td>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/$%7Busername%7D">HOME</a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/$%7Busername%7D/requestVM">REQUEST VM<br>
      </a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/$%7Busername%7D/listVMs">LIST MY VMS<br>
      </a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/$%7Busername%7D/pendingRequests">PENDING REQUESTS<br>
      </a></td>
    </tr>
  </tbody>
</table>

<br>

<br>

</body></html>