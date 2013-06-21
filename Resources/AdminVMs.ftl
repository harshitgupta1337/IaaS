<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>

  
  <meta content="text/html; charset=ISO-8859-1" http-equiv="content-type"><title>Admin Page</title></head><body>
<big><big><big>Admin Page<small><small><div style="text-align: right;"><a href="/">Logout</a><br><br>

<br>
</small></small></big></big></big>
<br>

<table style="text-align: left; width: 916px; margin-left: auto; margin-right: auto; height: 380px;" border="1" cellpadding="2" cellspacing="2">

  <tbody>
    <tr>
      <td colspan="1" rowspan="3" style="vertical-align: top; width: 80%;">
	
     
          <table style="text-align: left; width: 716px; margin-left: auto; margin-right: auto; height: 87px;" border="1" cellpadding="2" cellspacing="2">
        <tbody>
          <tr>
            <td style="vertical-align: top;">User's Name<br>
            </td>
            <td style="vertical-align: top;">Operating System<br>
            </td>
            <td style="vertical-align: top;">RAM<br>
            </td>
            <td style="vertical-align: top;">CPU<br>
            </td>
            <td style="vertical-align: top;">DISK<br>
            </td>
            <td style="vertical-align: top;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            </td>
            <td style="vertical-align: top;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <br>
            </td>
          </tr>
 <#list vms as vm>
	<tr>
            <td style="vertical-align: top;">${vm["name"]}<br>
            </td>
            <td style="vertical-align: top;">${vm["OS"]}<br>
            </td>
            <td style="vertical-align: top;">${vm["RAM"]}<br>
            </td>
            <td style="vertical-align: top;">${vm["CPU"]}<br>
            </td>
            <td style="vertical-align: top;">${vm["DISK"]}<br>
            </td>
            <td style="vertical-align: top;">
            <form method="post" action="/confirmVM/${vm['username']}/${vm['vmName']}" name="confirmAccount0"><button value="Confirm" name="Confirm">Confirm</button></form>
            <br>
            </td>
            <td style="vertical-align: top;">
            <form method="post" action="/rejectVM/${vm['username']}/${vm['vmName']}" name="rejectAccount0"><button value="Reject" name="Reject">Reject</button><br>
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
