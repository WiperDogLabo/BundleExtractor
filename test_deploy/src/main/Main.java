package main;

import java.util.ArrayList;
import java.util.List;

import bean.Bean;

public class Main {

	public static void main(String[] args) {
		new Main();
	}
	public Main(){
		List<Bean> beans = new ArrayList<Bean>();
		for(int i = 1;i<=10;i++){
			Bean bean = new Bean(String.valueOf(i), "name" + String.valueOf(i));
			beans.add(bean);
		}
		for (Bean bean : beans) {
			System.out.println(bean.toString());
		}
	}

}
