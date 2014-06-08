package attributes.relations;

import java.io.Serializable;

public interface ApproximateEquality<T extends Comparable<T>> extends Serializable {

	public boolean equal(T x, T y);
	
}
