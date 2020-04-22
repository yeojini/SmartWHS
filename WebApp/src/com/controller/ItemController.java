package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.data.ItemNameMapper;
import com.frame.Service;
import com.vo.InvoiceVO;
import com.vo.InvoicedetailVO;
import com.vo.ItemVO;

import msg.Msg;
import server.Client;
import server.Main;
import server.Sender;

@Controller
public class ItemController {
	@Resource(name = "itservice")
	Service<ItemVO> itservice;

	@Resource(name = "invservice")
	Service<InvoiceVO> invservice;

	@Resource(name = "invdtlservice")
	Service<InvoicedetailVO> invdtlservice;

	/* static 변수들 */
	public static String wareNameList[] = { "이천 제1물류창고" };
	private static Set<String> itMap = new HashSet<String>();
	private static Client client = null;
	private static Msg msg = null;
	private static ItemNameMapper itNameMapper = null;

	@RequestMapping("/itemregister.pc")
	public ModelAndView itemregister(ModelAndView mv, ItemVO newItem) {

		System.out.println("**" + newItem.toString());
		try {
			itservice.insert(newItem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mv.addObject("center", "itpage");
		mv.setViewName("main");

		return mv;
	}

	
	@RequestMapping("/getItemLoc.pc")
	@ResponseBody
	public ArrayList<String> getItemLoc(HttpServletResponse rs) {
		if(itMap.isEmpty()) { //혹시 비어 있다면 다시 확인!
			//item 다 불러와서 map 새로 짜기 
			ArrayList<ItemVO> itemList = null;
			try {
				itemList = itservice.selectAll(new ItemVO());
			} catch (Exception e) {
				e.printStackTrace();
			}
			for(ItemVO i : itemList) {
				itMap.add(i.getItemloc());
			}
		}
		
		ArrayList<String> ja = new ArrayList<String>();
		for(String m : itMap) {
			ja.add(m);
		}
		System.out.println(ja.toString());

		
		return  ja;
	}

	@RequestMapping("/itemsearch.pc")
	public void itemsearch(HttpServletResponse rs, ItemVO iv) {
		
		
		ArrayList<ItemVO> itemList = null;
		try {
			itemList = itservice.selectAll(iv);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("--" + itemList.toString());

		JSONArray ja = new JSONArray();
		for (ItemVO i : itemList) {
			JSONObject json = new JSONObject();
			json.put("itemid", i.getItemid());
			json.put("itemname", i.getItemname());
			json.put("itemcate", i.getItemcate());
			json.put("itemprice", i.getItemprice());
			json.put("itemweight", i.getItemweightpb());
			json.put("itemqtypb", i.getItemqtypb());
			json.put("wareid", i.getWareid());
			json.put("warename", i.getWarename());
			if(i.getItemloc() == null) {
				json.put("itemloc", "NULL");
			}else {
				json.put("itemloc", i.getItemloc());
				itMap.add(i.getItemloc());
			}
				
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
	public @ResponseBody String invoiceregister(ModelAndView mv, @RequestBody String ivJson) {
		if(itNameMapper == null) {
			itNameMapper = new ItemNameMapper();
		}
		
		System.out.println(ivJson);
		ArrayList<InvoicedetailVO> ivdList = new ArrayList<>();
		String response = null;
		try {
			JSONArray ivArr = new JSONArray(ivJson);
			for (int i = 0; i < ivArr.length(); i++) {
				JSONObject temp = (JSONObject) (ivArr.get(i));
				InvoicedetailVO ivd = new InvoicedetailVO();
				ivd.setItemid((String) temp.get("itemid"));
				ivd.setItemname((String) temp.get("itemname"));
				ivd.setWareid((String) temp.get("wareid"));
				ivd.setWarename((String) temp.get("warename"));
				ivd.setInvoicedtlqty(Integer.parseInt((String) temp.get("invoicedtlqty")));
				ivd.setInvoicestat((String) temp.get("invoicestat"));
				ivdList.add(ivd);

				//item location 가져오기 
				ItemVO tmpIt = new ItemVO();
				tmpIt.setItemid((String) temp.get("itemid"));
				try {
					tmpIt = itservice.select(tmpIt);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				System.out.println(tmpIt.getItemloc());
				/* TCP/IP 서버에 Task 전송 */
				sendTask(ivd.getInvoicestat(), ivd.getItemname(), ivd.getInvoicedtlqty(), tmpIt.getItemloc());

				
			}
			
			InvoiceVO newInvoice = new InvoiceVO();
			newInvoice.setDtllist(ivdList);

			invservice.insert(newInvoice);
			
			response = "SUCCESS";
		} catch (Exception e) {
			e.printStackTrace();
			response = "ERROR";
		}

		return response;

	}


	@RequestMapping("/invoicesearch.pc")
	public @ResponseBody ArrayList<InvoiceVO> invoicesearch(ModelAndView mv, HttpServletRequest request) {
		
		String empno = request.getParameter("empno");
		String empname = request.getParameter("empname");
		String startdate = request.getParameter("sd");
		String enddate = request.getParameter("ed");
		
		System.out.println("empno : " + empno + "empname : " + empname + "sd : " + startdate + ", ed : " + enddate);
		
		ArrayList<InvoiceVO> invList = null;
		InvoiceVO temp = new InvoiceVO();
		temp.setStartdate(startdate);
		temp.setEnddate(enddate);
		temp.setEmpno(empno);
		temp.setEmpno(empname);
		try {
			invList = invservice.selectAll(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//System.out.println(invList.get(1).getInvoicedate());
		return invList;

//		JSONArray ja = new JSONArray();
//		for (InvoiceVO i : invList) {
//			JSONObject json = new JSONObject();
//			json.put("invoiceid", i.getInvoiceid());
//			json.put("empno", i.getEmpno());
//			json.put("empname", i.getEmpname());
//			json.put("invoicedate", i.getInvoicedate());
//			ja.put(json);
//		}
		
//		rs.setContentType("text/html; charset=utf-8");
//		PrintWriter out;
//		try {
//			out = rs.getWriter();
//			out.print(ja.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@RequestMapping("/invoicedtlsearch.pc")
	public @ResponseBody ArrayList<InvoicedetailVO> invoicedtlsearch(ModelAndView mv, HttpServletRequest request) {
		
		String invoiceid = request.getParameter("invoiceid");

		System.out.println("invoiceid : " + invoiceid);
		
		ArrayList<InvoicedetailVO> invdtlList = null;
		InvoicedetailVO temp = new InvoicedetailVO();
		temp.setInvoiceid(Integer.parseInt(invoiceid));
		try {
			invdtlList = invdtlservice.selectAll(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		//System.out.println(invList.get(1).getInvoicedate());
		return invdtlList;

//		JSONArray ja = new JSONArray();
//		for (InvoiceVO i : invList) {
//			JSONObject json = new JSONObject();
//			json.put("invoiceid", i.getInvoiceid());
//			json.put("empno", i.getEmpno());
//			json.put("empname", i.getEmpname());
//			json.put("invoicedate", i.getInvoicedate());
//			ja.put(json);
//		}
		
//		rs.setContentType("text/html; charset=utf-8");
//		PrintWriter out;
//		try {
//			out = rs.getWriter();
//			out.print(ja.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void sendTask(String ivStat, String itName, int ivQty, String itLoc) {
		
		// 입고&출고 controller 설정
		int io = -1;
		if (ivStat.toUpperCase().equals("RECEIVING")) io = 1;
		else io = 0; 
		
		// 아이템 위치 (x,y) 좌표로 환산
		String[] itPoint = itNameMapper.getItemPoint(itLoc).split("_");
		int xPoint = Integer.parseInt(itPoint[1]);
		int yPoint = Integer.parseInt(itPoint[0]);
		
		System.out.println("x : " + xPoint + ", y : " + yPoint);
		
		msg = new Msg("Web", "ForkliftInfomatics");
		msg.setTask(io, itName, ivQty, xPoint, yPoint);

		String address = "70.12.113.200";
		if (client == null) {
			try {
				client = new Client(address, 9999);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Runnable r = new Sender(msg);
		Main.executorService.execute(r);
	}

}
