import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class sparse_vec {
	int Max =0;
	ConcurrentHashMap<Integer ,Double> vec = new ConcurrentHashMap<Integer ,Double>();

	boolean add(Double e)
	{
		if (e!=0)
			vec.put(Max, e);
		Max++;
		return true;
	}

	double get(int e)
	{
		if (e<Max)
			if (!vec.containsKey(e))
				return 0;
			else
				return vec.get( e);
		return 0;

	}

	boolean set ( Integer location,Double e )
	{
		if (e==0)
			vec.remove(location);
		else
		vec.put(location, e);
		return true;
	}
	int size(){
		return Max;
	}



}
