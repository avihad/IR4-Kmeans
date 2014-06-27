import java.util.ArrayList;


public class Main_test {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ArrayList<sparse_vec> vectors= new ArrayList<sparse_vec> ();
		sparse_vec tmp;
		tmp= new sparse_vec();
		tmp.add(1.0);
		tmp.add(1.0);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(1.0);
		tmp.add(1.1);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(1.1);
		tmp.add(1.0);
		vectors.add(tmp);
		
		
		tmp= new sparse_vec();
		tmp.add(3.0);
		tmp.add(3.0);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(3.0);
		tmp.add(3.1);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(3.1);
		tmp.add(3.0);
		vectors.add(tmp);
		
		tmp= new sparse_vec();
		tmp.add(30.0);
		tmp.add(30.0);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(30.0);
		tmp.add(30.1);
		vectors.add(tmp);
		tmp= new sparse_vec();
		tmp.add(30.1);
		tmp.add(30.0);
		
		vectors.add(tmp);
		k_mean local_kMean = new k_mean(vectors);
		local_kMean.calc_k_mean(3);
		
	}

}
