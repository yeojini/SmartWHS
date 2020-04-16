package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Service;
import com.vo.InvoiceVO;
import com.vo.InvoicedetailVO;
import com.vo.ItemVO;

import msg.Msg;
import server.Client;
import server.Main;
import server.Sender;

@Controller
@SessionAttributes("dtllist")	//model.addAttribute()를 사용해 객체를 저장할 경우 세션에 저장됨 (SessionStatus객체의 setComplete() 사용해서 지울 때까지)
public class ItemController {
	@Resource(name = "itservice")
	Service<ItemVO> itservice;
	
	@Resource(name = "invservice")
	Service<InvoiceVO> invservice;
	
	@Resource(name = "invdtlservice")
	Service<InvoicedetailVO> invdtlservice;
	
	/* static 변수들*/
	public static String wareNameList[] = {"이천 제1물류창고"};
	static Client client = null;
	static Msg msg = null;
	
	@RequestMapping("/itemregister.pc")
	public ModelAndView itemregister(ModelAndView mv, ItemVO newItem){

		
		System.out.println("**"+newItem.toString());
		try {
			itservice.insert(newItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.addObject("center", "itpage");
		mv.setViewName("main");

		return mv;
	}
	
	@RequestMapping("/itemsearch.pc")
	public void itemsearch(HttpServletResponse rs, ItemVO iv) {


		ArrayList<ItemVO> itemList = null;
		try {
			itemList =  itservice.selectAll(iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("--"+itemList.toString());
		
		JSONArray ja = new JSONArray(); 
		for(ItemVO i:itemList) {
			JSONObject json = new JSONObject();
			json.put("itemid", i.getItemid());
			json.put("itemname", i.getItemname());
			json.put("itemcate", i.getItemcate());
			json.put("itemprice", i.getItemprice());
			json.put("itemweigthpb", i.getItemweightpb());
			json.put("itemqtypb", i.getItemqtypb());
			json.put("wareid", i.getWareid());
			json.put("warename", i.getWarename());
			json.put("itemloc", i.getItemloc());
			json.put("itemstock", i.getItemstock());
			ja.put(json);
		}
		rs.setContentType("text/html; charset=utf-8");
		PrintWriter out;
		try {
			out = rs.getWriter();
			out.print(ja.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	@RequestMapping("/invoiceregister.pc")
	public @ResponseBody String invoiceregister(ModelAndView mv, @RequestBody String ivJson){
		
		System.out.println(ivJson);
		ArrayList<InvoicedetailVO> ivdList = new ArrayList<>();
		String response = null;
        try {
        	JSONArray ivArr = new JSONArray(ivJson);
        	for(int i = 0; i < ivArr.length(); i++) {
        		JSONObject temp = (JSONObject)(ivArr.get(i));
        		InvoicedetailVO ivd = new InvoicedetailVO();
        		ivd.setItemid((String)temp.get("itemid"));
        		ivd.setItemname((String)temp.get("itemname"));
        		ivd.setWareid((String)temp.get("wareid"));
        		ivd.setWarename((String)temp.get("warename"));
        		ivd.setInvoicedtlqty(Integer.parseInt((String)temp.get("invoicedtlqty")));
        		ivd.setInvoicestat((String)temp.get("invoicestat"));
        		ivdList.add(ivd);
        		
        		InvoiceVO newInvoice = new InvoiceVO();
            	newInvoice.setDtllist(ivdList);
            	
            	invservice.insert(newInvoice);
            
            	
            	/* TCP/IP 서버에 Task 전송 */
            	sendTask(ivd.getInvoicestat(), ivd.getItemname(), ivd.getInvoicedtlqty());
            	
            	response = "SUCCESS";
        	}
        } catch(Exception e) {
        	e.printStackTrace();
        	response = "ERROR";
        }
        
		return response;
		
	}
	
	
	@RequestMapping("/invoicesearch.pc")
	public ModelAndView invoicesearch(ModelAndView mv, InvoicedetailVO ivd) {
		
		System.out.println("!!!!"+ivd.toString());
		ArrayList<InvoicedetailVO> dtList = null;
		try {
			dtList = invdtlservice.selectAll(ivd);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		if(!dtList.isEmpty()) {
//			System.out.println("@@@" + dtList.toString());
//		}
		
		
		String ivTableHeader = "<thead>\r\n" + 
				"										<tr>\r\n" + 
				"											<th>Item ID</th>\r\n" + 
				"											<th>Item Name</th>\r\n" + 
				"											<th>Warehouse Name</th>\r\n" + 
				"											<th>Status</th>\r\n" + 
				"											<th>Qty</th>\r\n" + 
				"											<th>Date</th>\r\n" + 
				"										</tr>\r\n" + 
				"									</thead>";
		mv.addObject("ivTableHeader", ivTableHeader);
		mv.addObject("invoiceList", dtList);
		mv.addObject("center", "itpage");
		mv.setViewName("main");
		return mv;
		
		
	}
	
	public void sendTask(String ivStat, String itName, int ivQty){
		
		int io = -1;
    	if(ivStat.toUpperCase().equals("RECEIVING")) {
    		io = 1;
    	}else {
    		io = 0;
    	}
    	msg = new Msg("Web", "ForkliftInfomatics");
    	msg.setTask(io, itName, ivQty, 3, 6);
    	
		String address = "70.12.113.200";
    	if(client == null) {
    		try {
				client = new Client(address,9999);
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	Runnable r = new Sender(msg);
    	Main.executorService.execute(r);
	}
	
	
}
