<?php

namespace Model\Entities;

class Student
{
	use \Library\Shared;
	use \Library\Entity;

	public static function search(int $id = 0, string $fullname = '', string $guid = ''):self|null {
		$result;
		$db = self::getDB();
        $filters = [];
        foreach (['id', 'fullname', 'guid'] as $filter)
            if($$filter)
                $filters[$filter] = $$filter;
        
		$student = $db -> select(['Student' => []
			])->where(['Student' => $filters
			])->one();
		$class = __CLASS__;
		$result = $student ? new $class($student['fullname'], $student['division'], $student['guid'], $student['id']) : null;
		return $result;
	}

	public function save():self {
		$db = self::getDB();
		if (!$this->id)
			$this->id = $db -> insert([
				'Student' => [ 'fullname' => $this->fullname,
                                'division' => $this->division,
                                'guid' => $this->guid]
			])->run(true)->storage['inserted'];

		if ($this->_changed)
			$db -> update('Student', $this->_changed )
				-> where(['Student'=> ['id' => $this->id]])
				-> run();
		return $this;
	}

    public function getKeyboard(bool $full): array{
        $result = [];
        if($full){
            $result[] =[
                [
                    'title' => 'Всі оціки',
                    'id' => '321'
                ]
            ];
            $sql = "SELECT `stats`.`Subject`.* FROM `stats`.`Subject` WHERE `stats`.`Subject`.`id` = ANY(SELECT `stats`.`Mark`.`subject` FROM `stats`.`Mark` WHERE `stats`.`Mark`.`student` = $this->id)";
            $db = self::getDB();
            $db->request($sql);
            foreach ($db->result as $subject) {
                $result[] =[
                    [
                        'title' => $subject['name'],
                        'id' => $subject['id']
                    ]
                ];
            }
        }
        $result[] =[
            [
                'title' => 'Назад',
                'id' => '4321'
            ]
        ];
        return $result;
    }

	public function __construct(public string $fullname, public string $division = '', public string $guid = '', public int $id = 0) {
	}
}