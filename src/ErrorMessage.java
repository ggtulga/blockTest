
public class ErrorMessage {

	private static final String[] errorMessages = {
		"Хувьсагчдын нэрүүд таслалаар тусгаарлагдах ба нэр нь хоосон зай агуулахгүй!\n",  //0
		"Хувьсагчийн нэр зөвхөн Англи цагаан толгойн үсгүүд, тоо, доогуур зураас агуулсан байна. Гэхдээ тоогоор эхлэж болохгүй!\n", //1
		" хувьсагч зарлагдаагүй байна!\n", //2
		" хувьсагч аль хэдийн зарлагдсан байна!\n", //3
		"Тэмдэгтэн цуваа ' тэмдэгтээр төгсөх ёстой!\n", //4
		"Тэмдэгтэн цуваа \" тэмдэгтээр төгсөх ёстой!\n", //5
		"Блок хоосон байж болохгүй!\n", //6
		"Хүснэгтийн хэмжээ тогтмол тоо байна.!\n", //7
		"Хэрэглэгчээс нэг дор зөвхөн нэг л хувьсагчид утга авна!\n", //8
		"Тэмдэгтэн цувааны алдаа!\n", //9
		"Зөвхөн <=, >=, ==, <, >, != харьцуулах операторуудыг хэрэглэнэ үү!\n", //10
		"Бүх болокууд хоорондоо холбогдсон байх ёстой ба хамгийн сүүлд төгсгөлийн блоктой холбогдоно!\n", //11
		"Санах ой хүрэлцэхгүй байна!\n", //12
		"Ажиллах явцад санах ой дүүрлээ!\n", //13
		"Үл мэдэгдэх алдаа гарлаа!\n", //14
		" түлхүүр үгийг энд хэрэглэж болохгүй!\n", // 15
		"0 тоонд хуваах алдаа!\n", // 16
		"Хүснэгтийн индекст алдаа гарлаа!\n", // 17
	};

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
