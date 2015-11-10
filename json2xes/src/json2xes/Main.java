package json2xes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class Main {
	
    public static void main(String[] args) {
    	Map<Character, Set<Character>> map=new HashMap<Character, Set<Character>>();
    	Character[] arr0 = {'0'};   
    	Set<Character> set0 = new HashSet<Character>(Arrays.asList(arr0)); 
    	map.put('0', set0);
    	Character[] arr1 = {'0','1','2','3','4','5','6','7','8','9'};   
    	Set<Character> set1 = new HashSet<Character>(Arrays.asList(arr1)); 
    	map.put('1', set1);
    	Character[] arr2 = {'0','2','3','5','6','8','9'};   
    	Set<Character> set2 = new HashSet<Character>(Arrays.asList(arr2)); 
    	map.put('2', set2);
    	Character[] arr3 = {'3','6','9'};   
    	Set<Character> set3 = new HashSet<Character>(Arrays.asList(arr3)); 
    	map.put('3', set3);
    	Character[] arr4 = {'0','4','5','6','7','8','9'};   
    	Set<Character> set4 = new HashSet<Character>(Arrays.asList(arr4)); 
    	map.put('4', set4);
    	Character[] arr5 = {'0','5','6','8','9'};   
    	Set<Character> set5 = new HashSet<Character>(Arrays.asList(arr5)); 
    	map.put('5', set5);
    	Character[] arr6 = {'6','9'};   
    	Set<Character> set6 = new HashSet<Character>(Arrays.asList(arr6)); 
    	map.put('6', set6);
    	Character[] arr7 = {'0','7','8','9'};   
    	Set<Character> set7 = new HashSet<Character>(Arrays.asList(arr7)); 
    	map.put('7', set7);
    	Character[] arr8 = {'0','8','9'};   
    	Set<Character> set8 = new HashSet<Character>(Arrays.asList(arr8)); 
    	map.put('8', set8);
    	Character[] arr9 = {'9'};   
    	Set<Character> set9 = new HashSet<Character>(Arrays.asList(arr9)); 
    	map.put('9', set9);
    	
       
    	Scanner in = new Scanner(System.in);
    	int n=in.nextInt();
    	for(int i=0;i<n;i++){
    		String s=in.next();
    		//System.out.println("== =="+s);
    		while(!isok(s,map)){
    			s=minus1(s);
    		}
    		System.out.println(s);
    	}
    	in.close();
    }
    static boolean isok(String s,Map<Character, Set<Character>> map){
    	for(int i=1;i<s.length();i++){
    		char pre=s.charAt(i-1);
    		if(!map.get(pre).contains(s.charAt(i))){
    			return false;
    		}
    	}
    	return true;
    }
    static String minus1(String s){
    	StringBuffer result=new StringBuffer(s);
    	int len=s.length();
    	int n=len-1;
    	while(n>=0){
    	if(result.charAt(n)>'0'){
    		result.setCharAt(n, (char) (result.charAt(n)-1));
    		break;
    	}
    	else{
    		result.setCharAt(n, '9');
    		n=n-1;
    	}
    	}
    	int i=0;
    	while(result.charAt(i)=='0'){
    		i++;
    	}
    	return result.substring(i);
    }
}
