package mapperclasses;

public enum SourceType {

	CAR("Car"), FRIDGE("Fridge"), CLOCK("Clock");

	private String sourceType;

	public String sourceType() {
		return this.sourceType;
	}

	SourceType(String sourceType) {
		this.sourceType = sourceType;
	}

}
