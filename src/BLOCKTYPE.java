
public enum BLOCKTYPE {
	BEGIN("BEGIN"), END("END"), INIT("INIT"), INPUT("INPUT"), OUTPUT("OUTPUT"),IF("IF"),VALUE("VALUE");
	private String Value;
	private BLOCKTYPE(String value){
		this.Value=value;
	}
	public String getValue() {
		return Value;
	}

	
}
