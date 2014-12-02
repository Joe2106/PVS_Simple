package de.whs.simple;

public class Simple {
	static String separator="#";
	static String separator_replacement="\\s";
	
	//String Builders
	public static String SendLoginRequest(){
		return(Content.login + separator + "Please Send me your Username!" + "\n");}
	
	public static String SendLoginResponse(String Username){
		return(Content.login + separator + Username + "\n");}
	
	public static String SendNotifyNewUser(String Username){
		return(Content.notifyNewUser + separator + Username + "\n");}
	
	public static String SendNotifyUserLeft(String Username){
		return(Content.notifyUserLeft + separator + Username + "\n");}
	
	public static String SendLogout(){
		return(Content.logout + "\n");}
	
	public static String SendMessage(String Message){
		return(Content.send + separator + Message.replaceAll(separator, separator_replacement) + "\n");}
	
	public static String SendUserlistRequest(){
		return(Content.userlist + "\n");}
	
	public static String SendUserlistResponse(String[] Userlist){
		String erg = "";
		for(int i=0;i<Userlist.length;i++)
			erg=erg+separator+Userlist[i];
		return Content.userlist + erg + "\n";
	}
	
	public static String SendError(String Error){
		return Content.error + separator + Error + "\n";}
	
	
	
	//String Receiver
	public static Content getContentType(String received){
		String decoded[] = received.split(separator);
		return(Content.valueOf(decoded[0]));}
	
	private static String receiver(String received){
		String decoded[] = received.split(separator);
		return decoded[1];}
	
	public static String ReceiveLoginResponse(String received){
		return receiver(received);}
	
	public static String ReceiveNotify(String received){
		return receiver(received);}
	
	public static String ReceiveMessage(String received){
		return receiver(received.replaceAll(separator_replacement, separator));}
	
	public static String[] ReceiveUserlist(String received){
		String decoded[] = received.split(separator);
		String Userlist[] = new String[decoded.length-1];
		
		for(int i=1;i<decoded.length;i++)
			Userlist[i-1] = decoded[i];
		return Userlist;
	}
	
	public static String ReceiveError(String received){
		return receiver(received);}
}