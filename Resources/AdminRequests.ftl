<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>

  
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type"><title>Admin Page</title></head><body>
<big><big><big>Admin Page</big></big></big><div style="text-align: right;"><a href="/">Logout</a><br><br>
<br>

<br>

<table style="text-align: left; width: 916px; margin-left: auto; margin-right: auto; height: 380px;" border="1" cellpadding="2" cellspacing="2">

  <tbody>
    <tr>
      <td colspan="1" rowspan="3" style="vertical-align: top; width: 80%;">
	
     
          <table style="text-align: left; width: 716px; margin-left: auto; margin-right: auto; height: 87px;" border="1" cellpadding="2" cellspacing="2">
        <tbody>
          <tr>
            <td style="vertical-align: top;">Name<br>
            </td>
            <td style="vertical-align: top;">Username<br>
            </td>
            <td style="vertical-align: top;">Email ID<br>
            </td>
            <td style="vertical-align: top;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            </td>
            <td style="vertical-align: top;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            </td>
          </tr>
 <#list pendingUsers as user>
	<tr>
            <td style="vertical-align: top;">${user["name"]}<br>
            </td>
            <td style="vertical-align: top;">${user["username"]}<br>
            </td>
            <td style="vertical-align: top;">${user["email"]}<br>
            </td>
            <td style="vertical-align: top;">
            <form method="post" action="/confirmAccount/${user["username"]}" name="confirmAccount0"><button value="Confirm" name="Confirm">Confirm</button></form>
            <br>
            </td>
            <td style="vertical-align: top;">
            <form method="post" action="/rejectAccount/${user["username"]}" name="rejectAccount0"><button value="Reject" name="Reject">Reject</button><br>
            </form>
            </td>
          </tr>
</#list>
        </tbody>
      </table>
      </td>
      <td style="vertical-align: middle; text-align: center;"><a href="/admin">HOME</a><br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/admin/accounts">ACCOUNT REQUESTS</a><br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/admin/vms">VM REQUESTS</a><br>
      </td>
    </tr>

  </tbody>
</table>

<br>

<br>


</body></html>
