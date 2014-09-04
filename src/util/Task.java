package util;

public class Task {
	public int amount;
	
	public String getType(){
		return "Task";
	}
	
	public Task copyTask(){
		Task t = this;
		if (t instanceof openAccount) {
			openAccount o = new openAccount();
			o.custName = ((openAccount) t).custName;
			o.amount = t.amount;
			return o;
		}
		if (t instanceof deposit) {
			deposit d = new deposit();
			d.amount = t.amount;
			d.accountNumber = ((deposit) t).accountNumber;
			d.passWord = ((deposit) t).passWord;
			return d;
		}
		if (t instanceof withdrawal) {
			withdrawal w = new withdrawal();
			w.amount = t.amount;
			w.accountNumber = ((withdrawal) t).accountNumber;
			w.passWord = ((withdrawal) t).passWord;
			return w;
		}
		if (t instanceof takeLoan) {
			takeLoan ta = new takeLoan();
			ta.amount = t.amount;
			ta.custName = ((takeLoan) t).custName;
			return ta;
		}
		if (t instanceof rob) {
			rob r = new rob();
			r.amount = t.amount;
			return r;
		}
		return null;
	}
	
	
	
	
	public String toString(){
		Task t = this;
		if (t instanceof openAccount) {
			return "openAccount";
		}
		if (t instanceof deposit) {
			return "deposit";
		}
		if (t instanceof withdrawal) {
			return "withdrawal";
		}
		if (t instanceof takeLoan) {
			return "takeLoan";
		}
		if (t instanceof rob) {
			return "rob";
		}
		return null;
	}

}




