<%@page import="java.sql.*"%>
<%@page import="model.*"%>
<%@page import="java.util.*"%>

<%
    Harvest harvest = (Harvest) request.getAttribute("harvest");
    List<Parcel> parcels = (List<Parcel>) request.getAttribute("parcels");
    List<Harvest_data> data = (List<Harvest_data>) request.getAttribute("data");
    float got = (float) request.getAttribute("got");
    float forecast = (float) request.getAttribute("forecast");
    
    HashMap<String, Float> fertilizer_rank = (HashMap<String, Float>) request.getAttribute("npk_hash");
    List<Npk> npk_rank = (List<Npk>)request.getAttribute("npk_rank");
    List<Parcel> parcels_rank = (List<Parcel>) request.getAttribute("parcel_rank");
     List<Npk> npk_rank_price = (List<Npk>)request.getAttribute("npk_price_rank");
%>

<div class="row justify-content-between">
    <div class="col-lg-6 col-md-6">
        <div class="card" style="padding: 3% 3% 3% 3%">
            <div class="card-body">
                <div class="card-title" style="margin-bottom: 10px;">
                    <h3 class="title-3"> Harvest data from <span class="font-weight-bold"> <%= harvest.getName_harvest() %> </span> </h3>
                </div>
                <form action="Add_Harvest_data_ctrl" method="get" novalidate="novalidate" style="padding: 3% 3% 3% 3%">
                    <input type="hidden" name="harvest_id" value="<%= harvest.getId_harvest() %>" ></input>
                    <!--C HOOSING PARCEL --> 
                    <div class="row form-group align-items-center">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Choose parcel to enter data  :</label>
                        </div>
                        <div class="col-12 col-md-9">
                            <select name="parcel_id" id="SelectLm" class="form-control-sm form-control">
                               <% if(parcels != null){
                                   for(Parcel parcel : parcels){ %>
                                   <option value="<%= parcel.getId_parcel() %>"> <%= parcel.getName_parcel() %> </option>
                                   <% }
                                    } %>   
                            </select>
                        </div>
                    </div>

                    <!-- AMOUNT OF CORNCOB RECEIVED --> 
                    <div class="row form-group align-items-center">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Corncob received : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="corncob_received" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>

                    <!-- WEIGHT --> 
                    <div class="row form-group align-items-center">
                        <div class="col col-md-3">
                            <label for="selectSm" class=" form-control-label"> Total weight : </label>
                        </div>
                        <div class="col-12 col-md-9">
                            <input id="cc-pament" name="weight" type="number" class="form-control" aria-required="true" aria-invalid="false">
                        </div>
                    </div>

                    <div>
                        <button id="payment-button" type="submit" class="btn  btn-info">
                            Save
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <div class="col-md-6 col-lg-6">
         <div class="card" style="padding: 3% 3% 3% 3%">
             <div class="card-body">
                <h3 class="title-3" style="margin-bottom: 2%"> Forecast result from data above : </h3>
                <table class="table table-borderless" style="width: 80%">
                    <thead>
                        <th> Parcel </th>
                        <th> Corncob </th>
                        <th> Notice </th>
                        <th> Weight </th>
                    </thead>
                    <tbody>
                        <% 
                        if(data != null){
                            for(Harvest_data hd : data) { %>
                            <tr>
                                <td> <%= hd.getId_parcel() %>  </td>
                                <td> 
                                     <%= hd.getCorncob_received() %> 
                                </td>
                                <td> <%= hd.get_harvest_data_notice(null) %> </td>
                                <td> Kg <%= hd.getWeight_received() %>  </td>
                            </tr>
                            <% } 
                        } %>

                    </tbody>
                </table>
                <div style="margin-top: 5%">
                    <table class="table table-info">
                        <thead>
                            <th> Already have </th>
                            <th> Total forecast </th>
                        </thead>
                        <tr>
                            <td> Kg <%= got %> </td>
                            <td> <span class="font-weight-bold"> Kg <%= forecast %> </span>  </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
                    
<div class="row justify-content-between" style="margin-top: 20px">
    <div class=" col-lg-6 col-md-6">
        <div class="card">
            <div class="card-body">
               <h3 class="title-3" style="margin-bottom: 2%"> Fertilizer rank </h3>
               <table class="table table-borderless" style="width: 80%">
                   <thead>
                       <th> Fertilizer </th>
                       <th> Used </th>
                       <th> Total production </th>
                       <th> Ratio </th>
                   </thead>
                   <tbody>
                       <% 
                       if(npk_rank != null && fertilizer_rank != null){
                           for(int k = npk_rank.size()-1; k >= 0; k--) { 
                              Npk pr = npk_rank.get(k);     %>
                           <tr>
                               <td> <%= pr.getId_npk() %>  </td>
                               <td> 
                                   <%= pr.my_production_on_harvest(harvest, null) %> 
                               </td>
                               <td> <%= forecast %> </td>
                               <td> Kg <%= fertilizer_rank.get(pr.getId_npk()) %>  </td>
                           </tr>
                           <% } 
                       } %>
                   </tbody>
               </table>
            </div>
        </div>
    </div>
                   
    <div class=" col-lg-6 col-md-6">
        <div class="card">
            <div class="card-body">
               <h3 class="title-3" style="margin-bottom: 2%"> PARCEL RANK </h3>
               <table class="table table-borderless" style="width: 80%">
                   <thead>
                       <th> Parcel </th>
                       <th> NPK_A </th>
                       <th> NPK_B </th>
                       <th> NPK_C </th>
                       <th> Ratio </th>
                   </thead>
                   <tbody>
                       <% 
                       if(parcels_rank != null){
                           for(int i = parcels_rank.size()-1; i >=0 ; i--) { 
                               Parcel parc = parcels_rank.get(i);
                           HashMap<String, Float> t = parc.my_npk_percentage();
                       %>
                           <tr>
                               <td> <%= parc.getId_parcel() %>  </td>
                               <td> 
                                   <%= t.get("NPK_A") %> 
                               </td>
                               <td> 
                                   <%= t.get("NPK_B") %> 
                               </td>
                               <td> 
                                   <%= t.get("NPK_C") %> 
                               </td>
                               <td> <%= parc.get_corncob_weight_ratio(null) %> </td>
                           </tr>
                           <% } 
                       } %>
                   </tbody>
               </table>
            </div>
        </div>
    </div>   
</div>   
                   
            
 <div class="row col-lg-12 col-md-12 justify-content-between" style="margin-top: 20px">               
      <div class=" col-lg-12 col-md-12">
        <div class="card">
            <div class="card-body">
               <h3 class="title-3" style="margin-bottom: 2%"> Fertilizer rank by price ration </h3>
               <table class="table table-borderless" style="width: 80%">
                   <thead>
                       <th> Fertilizer </th>
                       <th> Qty bought </th>
                       <th> Average pu </th>
                       <th> Total </th>
                       <th> Prod result </th>
                       <th> Ratio </th>
                   </thead>
                   <tbody>
                       <% 
                       if(npk_rank_price != null){
                           for(int k = npk_rank_price.size()-1; k >= 0; k--) { 
                              Npk prr = npk_rank_price.get(k);     %>
                           <tr>
                               <td> <%= prr.getId_npk() %>  </td>
                               <td> 
                                   <%= prr.my_qty_bought() %> 
                               </td>
                               <td> <%= prr.my_average_price() %> </td>
                               <td> <%= prr.my_total_expense()%>  </td>
                               <td> <%= prr.my_production_on_harvest(harvest, null) %>  </td>
                               <td> <%= prr.ratio_qty_price(harvest) %>  </td>
                           </tr>
                           <% } 
                       } %>
                   </tbody>
               </table>
            </div>
        </div>
    </div>             
 </div>