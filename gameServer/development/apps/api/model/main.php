<?php
/**
 * User Controller
 *
 * @author Serhii Shkrabak
 * @global object $CORE->model
 * @package Model\Main
 */



namespace Model;
class Main
{
	use \Library\Shared;

	public function formsubmitAmbassador(string $firstname = '', string $secondname = '', string $position = '', string $phone = '', string $email = ''):?array {
		// Тут модель повинна бути допрацьована, щоб використовувати бази даних, тощо
		$key = $this->getVar('TOCKEN', 'e'); // Ключ API телеграм
		if($key == '')
			throw new \Exception('key', 6);
		$result = null;
		$chat = 785442631;
		$text = "Нова заявка в *Цифрові Амбасадори*:\n" . $firstname . ' '. $secondname. ', '. $position . "\n*Зв'язок*: " . $phone . "\n*Email*: " . $email;
		$text = urlencode($text);
		$answer = file_get_contents("https://api.telegram.org/bot$key/sendMessage?parse_mode=markdown&chat_id=$chat&text=$text");
		$answer = json_decode($answer, true);
		$result = ['message' => $answer['result']];
		return $result;
	}
	
	public function tgwebhook(array $data):?array {
		if ($data['token'] == $this->getVar('TGToken', 'e')) {
			$input = $data['input'];
			$input = json_decode( $input, true );

			file_put_contents(ROOT . "media/log.txt", file_get_contents('php://input') . "\n\n", FILE_APPEND);

			if (isset($input['callback_query'])) {
				$this->TG->process($input['callback_query']['message'], $input['callback_query']['data']);
			}
			else
				if (isset($input['edited_message']))
					$this->TG->process($input['edited_message'], edited: true);
				else
					if (isset($input['message']))
						$this->TG->process($input['message']);
					else
						if (isset($input['my_chat_member'])) {
							$update = $input['my_chat_member'];
							$this->TG->alert("\n*" . $update['new_chat_member']['status'] . ":* " . $update['chat']['first_name'] . ' ' . $update['chat']['last_name']);
						}
						else
							$this->TG->alert($data['input']);
		} else
			throw new \Exception('TG token incorrect', 3);
		return [];
	}

	public function uniwebhook(string $type = '', string $value = '', int $code = 0):?array {
		
		
		$student = \Model\Entities\Student::search(guid: $this->getVar('user'));
		if(!isset($student)){
			// $url = 'https://account.pnit.od.ua/account/list';
			// $data = ['user' => $this->getVar('user'), 'type' => 'user'];

			// // use key 'http' even if you send the request to https://...
			// $options = [
			// 	'http' => [
			// 		'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
			// 		'method'  => 'POST',
			// 		'content' => http_build_query($data)
			// 	]
			// ];
			// $context  = stream_context_create($options);
			// $res = file_get_contents($url, false, $context);
			// if ($res === FALSE) { 
			// 	throw new \Exception('lol', 3);
			// }
			// $res = json_decode($res, true);
			// $res = $res['data'];
			// $fullname = $res['surname'] . $res['name'] . $res['middlename'];
			// $student = (new \Model\Entities\Student(guid: $this->getVar('user'), fullname: $fullname))->save();
			$student = (new \Model\Entities\Student(guid: $this->getVar('user'), fullname: "Жмых Вжух Вжухович"))->save();
		}
		
		$mess = '';
		$keyboard = true;
		if ($type == 'message' && $value == '/start'){
			$mess = 'Виберіть за яким предметом потрібна оцінка';
		}elseif ($type == 'click') {
			switch ($code) {
				case '4321':
					$result = ['type' => 'context', 'set' => null];
					break;
				case '321':
					$keyboard = false;
					foreach (\Model\Entities\Mark::search(student: $student->id) as $mark) {
						$subject = \Model\Entities\Subject::search(id: $mark->subject);
						$mess .= "$subject->name: $mark->value\n";
					}
					break;
				default:
					$keyboard = false;
					$mark = \Model\Entities\Mark::search(student: $student->id, subject: $code);
					$subject = \Model\Entities\Subject::search(id: $code);
					$mark = $mark['0'];
						$mess .= "$subject->name: $mark->value";
					break;
			}
		}
		if(!isset($result))
			$result = [
				'type' => 'message',
				'value' => $mess,
				'to' => $this->getVar('user'),
				'keyboard' => [
                    'inline' => true,
                    'buttons' => $student->getKeyboard($keyboard)
                ]
			];
        return $result;
		
	}


	public function googlegetData(int $count = 0):?array {
		

		require_once ROOT . 'vendor/autoload.php';
		require_once ROOT . 'simple-xlsx/simplexlsx.class.php'; 

		$client = new \Google_Client();
		$client->setApplicationName('Get Files');
		putenv('GOOGLE_APPLICATION_CREDENTIALS=./credential.json');
		$client->addScope(\Google_Service_Drive::DRIVE);
		$client->useApplicationDefaultCredentials();

		$folderId = '1Lzk5c5gAtJ8cEqaZWCtPRKwhKDENqybg';

		$optParams = array (
			'q' => "'". $folderId ."' in parents",
			'fields' => 'files(id, name)'
		);

		$service = new \Google_Service_Drive($client);

		$results = $service->files->listFiles($optParams);

		//var_dump(count($results->getFiles()));

		if(count($results->getFiles()) > 0){
			foreach($results->getFiles() as $key=>$file){
				// echo $file->getId() . '|' . $file->getName();
				if($file->getId()){
					$content = $service->files->get($file->getId(), ['alt' => 'media']);
					file_put_contents( 'file.xlsx', $content->getBody()->getContents());
					$xlsx = new \SimpleXLSX( ROOT . 'file.xlsx');
					$name = $xlsx->getCell(0, 'C11');
					$lecturer = $xlsx->getCell(0, 'D13');
					$subject = Entities\Subject::search(name: $name);
					if(!$subject)
						$subject = (new Entities\Subject(name: $name, lecturer: $lecturer))->save();
					// var_dump($subject);
					$i = 18;
					while ($xlsx->getCell(0, 'B'.$i) != '') {
						$student = Entities\Student::search(fullname: $xlsx->getCell(0, 'B'.$i) . ' ' . $xlsx->getCell(0, 'C'.$i));
						if(!$student)
							$student = (new Entities\Student(fullname: $xlsx->getCell(0, 'B'.$i) . ' ' . $xlsx->getCell(0, 'C'.$i), division: $xlsx->getCell(0, 'H7')))->save();
						var_dump($student);
						$mark = (new Entities\Mark(student: $student->id, subject: $subject->id, value: $xlsx->getCell(0, 'E'.$i)))->save();
						var_dump($mark);
						++$i;
					}

				}
			}
		}
		
        
		
		$result =[];

        return $result;
	}


	public function buildget(string $value):?array {
		// echo $value;
		$value = json_decode($value, true);
		$string = json_encode($value['coords']);
		$build = new Entities\Build($value['maket'], $string);
		$build->save();

		$makets = Entities\Build::search('less');
		// var_dump($maket['0']);
		file_put_contents( $value['maket'], $string);
		$result = [];
        return $makets;
	}

	public function marksloadData():?array {
		// echo $value;
		// $value = json_decode($value, true);
		// $string = json_encode($value['coords']);
		$mark = new Entities\Mark(1, 1, 88);
		$mark->save();

		$marks = Entities\Mark::search(1, 1);
		// var_dump($maket['0']);
		// file_put_contents( $value['maket'], $string);
		echo $marks[0]->value ;
		$result = [];
        return $result;
	}

	public function __construct() {
		$this->db = new \Library\MySQL('Game',
            \Library\MySQL::connect(
                $this->getVar('DB_HOST', 'e'),
                $this->getVar('DB_USER', 'e'),
                $this->getVar('DB_PASS', 'e')
            ) );
    	$this->setDB($this->db);
	}
}