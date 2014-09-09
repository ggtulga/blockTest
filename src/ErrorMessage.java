
public class ErrorMessage {

	private static final String[] errorMessages = {
		"Хувьсагчдын нэрүүд таслалаар тусгаарлагдах ба нэр нь хоосон зай агуулахгүй!\n",
		"Хувьсагчийн нэр зөвхөн Англи цагаан толгойн үсгүүд болон тоог агуулсан байна. Гэхдээ тоогоор эхлэж болохгүй!\n",
		" хувьсагч зарлагдаагүй байна!\n",
		" хувьсагч аль хэдийн зарлагдсан байна!\n",
		"Тэмдэгтэн цуваа ' тэмдэгтээр төгсөх ёстой!\n",
		"Тэмдэгтэн цуваа \" тэмдэгтээр төгсөх ёстой!\n",
		"Блок хоосон байж болохгүй!\n",
		"Хүснэгтийн хэмжээ тогтмол тоо байна.!\n",
		"Хэрэглэгчээс нэг дор зөвхөн нэг л хувьсагчид утга авна!\n",
		"Тэмдэгтэн цувааны алдаа!\n",
		"Зөвхөн <=, >=, ==, <, >, != харьцуулах операторуудыг хэрэглэнэ үү.",
		"Бүх болокууд хоорондоо холбогдсон байх ёстой ба хамгийн сүүлд төгсгөлийн блоктой холбогдоно!\n",
	};

	int errno;
	DrawableBlock v;
	String msg;
	
	public ErrorMessage(int errno, DrawableBlock block)
	{
		msg = errorMessages[errno];
		v = block;
	}

	public ErrorMessage(String prefix, int errno, DrawableBlock block)
	{
		msg = prefix + errorMessages[errno];
		v = block;
	}
	

	public void setBlock(DrawableBlock block) { v = block; }
	public DrawableBlock getBlock() { return v; }

	public void setMsg(String m) { msg = m; }
	public String getMsg() { return msg; }
};
