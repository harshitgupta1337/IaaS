<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>

<meta content="text/html; charset=ISO-8859-1" http-equiv="content-type"><title>User Home</title></head><body><big><big><big><big>Welcome ${username}</big></big></big></big><div style="text-align: right;"><a href="/">Logout</a><br><br>
<br>
<br>
<table style="text-align: left; width: 100%; height: 397px; margin-left: auto; margin-right: auto;" border="1" cellpadding="2" cellspacing="2">
  <tbody>
    <tr>
      <td colspan="1" rowspan="4" style="vertical-align: top; width: 80%;">Kindly choose the configuration of your requirement : <br>
      <br>
      <form method="post" action="/account/${username}/vmRequested" name="vmRequest">Operating System : 
        <select name="OperatingSystem">
        <option value="emi-FCCA3D9E#Ubuntu 12.04 x86_64">Ubuntu 12.04 x86_64</option>
        </select>
        <br>
        <br>
Hardware Configurations : 
        <select name="Hardware">
        <option value = "m1.small">1 CPU, 128 MB RAM, 2 DISKS</option>
        <option value = "c1.medium">1 CPU, 256 MB RAM, 5 DISKS</option>
        <option value = "m1.large">2 CPU, 512 MB RAM, 10 DISKS</option>
        <option value = "m1.xlarge">2 CPU, 1024 MB RAM, 20 DISKS</option>
        <option value = "c1.xlarge">4 CPU, 2048 MB RAM, 20 DISKS</option>
        </select>
        <br>
        <br>
VM Name : <input name="name"><br>
        <br>
        <br>
        <button value="requestVM" name="requestVM">Request VM</button><br>
      </form>
</td>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/${username}">HOME</a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/${username}/requestVM">REQUEST VM<br>
      </a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/${username}/listVMs">LIST MY VMS<br>
      </a></td>
    </tr>
    <tr>
      <td style="vertical-align: middle; text-align: center;"><a href="/account/${username}/pendingRequests">PENDING REQUESTS<br>
      </a></td>
    </tr>
  </tbody>
</table>
<br>



</body></html>