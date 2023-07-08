<%@page import="java.sql.*"%>
<%@page import="model.*"%>
<%@page import="java.util.*"%>

<% 
    List<Parcel> parcels = (List<Parcel>) request.getAttribute("parcels"); 
    Parcel main_parcel = (Parcel) request.getAttribute("parcel");
    Check_parcel main_check = (Check_parcel) request.getAttribute("main_check");
    List<Check_parcel> checks = (List<Check_parcel>) request.getAttribute("checks");
    HashMap<String, String[]> anomalies = (HashMap<String, String[]>) request.getAttribute("anomalies");

%>


<div class="row justify-content-between">
    <div class="col-lg-8 col-md-8">
        <div class="card" style="padding: 3% 3% 3% 3%">
            <div class="card-body">
                <div class="card-title" style="margin-bottom: 50px;">
                    <h3> Checking data of <span class="font-weight-bold"> <%= main_parcel.getName_parcel() %> </span>  </h3>
                </div>
                <form action="Checking_parcel_ctrl" method="get" novalidate="novalidate">
                    <!-- CHOOSING PARCEL --> 
                    <div class="row form-group align-items-center">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Choose parcel  :</label>
                        </div>
                        <div class="col-9 col-md-6">
                            <select name="parcel_id" id="SelectLm" class="form-control-sm form-control">
                                <% for(Parcel p : parcels){ %>
                                <option value="<%= p.getId_parcel() %>" > <%= p.getName_parcel() %> </option>
                                <% } %>
                            </select>
                        </div>
                        <button class="col-3 col-md-3 btn  btn-info" id="payment-button" type="submit">
                            Search
                        </button>
                    </div>
                </form>
                
                <div style="margin-top: 10%;">
                    <h3 class="title-3"> Checking result from : <span class="font-weight-bold"> <%= main_check.getDate_check() %>  </span> </h3>
                    
                    <form action="Checking_parcel_ctrl" method="get" novalidate="novalidate">
                        <!--PARCELLE ID--> 
                        <input type="hidden" name="parcel_id" value="<%= main_parcel.getId_parcel() %>"> </input>

                        <!-- CHOOSING PARCEL --> 
                        <div class="row form-group align-items-center">
                            <div class="col col-md-3">
                                <label for="selectSm" class=" form-control-label"> Choose date checking : </label>
                            </div>
                            <div class="col-9 col-md-6">
                                <select name="checking_data_id" id="SelectLm" class="form-control-sm form-control">
                                    <% if(checks != null){
                                        for(Check_parcel cp : checks){ %>
                                        <option value="<%= cp.getId_check_parcel() %>" > <%= cp.getDate_check() %> </option>
                                        <% }
                                        } %>
                                </select>
                            </div>
                            <button class="col-3 col-md-3 btn  btn-info" id="payment-button" type="submit">
                                Search
                            </button>
                        </div>
                    </form>
                    
                    <table class="table table-striped">
                        <thead>
                            <th> Designation </th>
                            <th> Checking data </th>
                            <th> Previous check <% if(anomalies != null) out.print(((String[]) anomalies.get("date"))[0]); %> </th>
                            <th> Notice </th>
                        </thead>
                        <tbody>
                            <% if(anomalies != null){ %>                   
                                <tr>
                                    <% String[] temp1 = (String[]) anomalies.get("feet"); %>
                                    <td> Feet amount </td>
                                    <td> <%= main_check.getFeet_amount() %> </td>
                                    <td> <%= temp1[0] %> </td>
                                    <td> <%= temp1[1] %> </td>
                                </tr>
                                <tr>
                                    <% String[] temp2 = anomalies.get("corncob"); %>
                                    <td> Corncob </td>
                                    <td> <%= main_check.getAverage_corncob() %> </td>
                                    <td> <%= temp2[0] %> </td>
                                    <td> <%= temp2[1] %> </td>
                                </tr>
                                <tr>
                                    <% String[] temp3 = anomalies.get("color"); %>
                                    <td> Color </td>
                                    <td> <%= main_check.getColor() %> </td>
                                    <td> <%= temp3[0] %> </td>
                                    <td> <%= temp3[1] %> </td>
                                </tr>
                                <tr>
                                    <% String[] temp4 = anomalies.get("growth"); %>
                                    <td> Growth </td>
                                    <td> <%= main_check.getGrowth() %> </td>
                                    <td> <%= temp4[0] %> </td>
                                    <td> <%= temp4[1] %> </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                
            </div>
        </div>
    </div>
    <div class="col-md-4 col-lg-4 grid-margin stretch-card">
        <div class="card-people">
          <img src="asset/images/corn2.png" alt="people">
        </div>
    </div>
</div>