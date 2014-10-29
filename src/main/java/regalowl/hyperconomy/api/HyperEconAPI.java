package regalowl.hyperconomy.api;


import regalowl.hyperconomy.HyperConomy;
import regalowl.hyperconomy.account.HyperPlayer;
import regalowl.hyperconomy.util.LanguageFile;

public class HyperEconAPI implements EconomyAPI {

	public boolean checkFunds(double money, HyperPlayer player) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(player.getName())) {
			return false;
		}
		return hc.getDataManager().getAccount(player.getName()).hasBalance(money);
	}
	
	public boolean checkFunds(double money, String name) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(name)) {
			return false;
		}
		return hc.getDataManager().getAccount(name).hasBalance(money);
	}

	public void withdraw(double money, HyperPlayer player) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().hyperPlayerExists(player.getName())) {
			return;
		}
		player.withdraw(money);
	}

	public void withdrawAccount(double money, String name) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(name)) {
			return;
		}
		hc.getDataManager().getAccount(name).withdraw(money);
	}

	public void deposit(double money, HyperPlayer player) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().hyperPlayerExists(player.getName())) {
			return;
		}
		player.deposit(money);
	}

	public void depositAccount(double money, String name) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(name)) {
			return;
		}
		hc.getDataManager().getAccount(name).deposit(money);
	}

	public void setBalance(double balance, String name) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(name)) {
			return;
		}
		hc.getDataManager().getAccount(name).setBalance(balance);
	}

	public boolean checkAccount(String name) {
		HyperConomy hc = HyperConomy.hc;
		return hc.getDataManager().accountExists(name);
	}

	public double getBalance(String account) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(account)) {
			return 0.0;
		}
		return hc.getDataManager().getAccount(account).getBalance();
	}

	public boolean createAccount(String account) {
		HyperConomy hc = HyperConomy.hc;
		if (!hc.getDataManager().accountExists(account)) {
			HyperPlayer hp = hc.getHyperPlayerManager().addPlayer(account);
			if (hp != null) {
				return true;
			}
		}
		return false;
	}
	
	public String formatMoney(double money) {
		HyperConomy hc = HyperConomy.hc;
		LanguageFile L = hc.getLanguageFile();
		return (L.formatMoney(money));
	}

	public int fractionalDigits() {
		return -1;
	}

	public String currencyName() {
		return "";
	}

	public String currencyNamePlural() {
		return "";
	}
}
