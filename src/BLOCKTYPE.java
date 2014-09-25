
public enum BLOCKTYPE {
	BEGIN("BEGIN"), END("END"), INIT("INIT"), INPUT("INPUT"), OUTPUT("OUTPUT"), IF("IF"), VALUE("VALUE"), POINT("POINT");
	private String value;
	private BLOCKTYPE(String value){
		this.value = value;
	}
	public String getValue() {
		return value;
	}


}
