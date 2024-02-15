package com.hatib.ckeecommerce.Global;

import java.util.ArrayList;
import java.util.List;

import com.hatib.ckeecommerce.model.Product;

public class GlobalData {

	public static List<Product> cart;
	
	static {
		cart = new ArrayList<>();
	}
}
