<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head><meta content="text/html; charset=ISO-8859-1" http-equiv="content-type"><title>VMDetails.html</title></head><body><big><big><big><big>Virtual Machine Details</big></big></big></big><br>
<br>
<table style="text-align: left; width: 100%;" border="1" cellpadding="2" cellspacing="2">
  <tbody>
    <tr>
      <td style="vertical-align: top;">Name<br>
      </td>
      <td style="vertical-align: top;">Operating System<br>
      </td>
      <td style="vertical-align: top;">RAM<br>
      </td>
      <td style="vertical-align: top;">CPU<br>
      </td>
      <td style="vertical-align: top;">Disk<br>
      </td>
      <td style="vertical-align: top;">Public IP Address<br>
      </td>
    </tr>
    <tr>
      <td style="vertical-align: top;">${vmName}<br>
      </td>
      <td style="vertical-align: top;">${os}<br>
      </td>
      <td style="vertical-align: top;">${ram}<br>
      </td>
      <td style="vertical-align: top;">${cpu}<br>
      </td>
      <td style="vertical-align: top;">${disk}<br>
      </td>
      <td style="vertical-align: top;">${ip}<br>
      </td>
    </tr>
  </tbody>
</table>
<br>


<br>
<br>
<big style="text-decoration: underline;"><big>Secure Shell Login Details</big></big><br>
<br>
Username : ${loginName}<br>
<br>
<br>
Private key : <br>
<br>
<#if PrivateKey="Not Available">
	Not Available
<#else>
	<#list lines as line>
		${line}<br>
	</#list>
</#if>
<br>
</body></html>