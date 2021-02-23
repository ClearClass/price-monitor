package in.clearclass.entity;

public class Product {
	private Integer partnum;
	private String name;
	
	public Product(int partnum, String name) {
		this.partnum = partnum;
		this.name = name.trim();
	}
	
	public Product(String partnum, String name) {
		this.partnum = partnum.isEmpty()? null : Integer.parseInt(partnum);
		this.name = name.trim();
	}

	public int getPartnum() {
		return partnum;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((partnum == null) ? 0 : partnum.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object ob) {
		if (this == ob)
			return true;
		if (ob == null)
			return false;
		if (getClass() != ob.getClass())
			return false;
		Product other = (Product) ob;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partnum == null) {
			if (other.partnum != null)
				return false;
		} else if (!partnum.equals(other.partnum))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return partnum + "_" + name;
	}
}