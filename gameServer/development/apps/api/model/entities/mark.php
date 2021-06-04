<?php

namespace Model\Entities;

class Mark
{
	use \Library\Shared;
	use \Library\Entity;

	public static function search(int $student = 0, int $subject = 0):array|null {
		$result = [];
		$db = self::getDB();
        $filters = [];
        foreach (['student', 'subject'] as $filter)
            if($$filter)
                $filters[$filter] = $$filter;
        
		foreach ($db -> select([
				'Mark' => []
			])->where([
				'Mark' => $filters
			])->many() as $mark) {
				$class = __CLASS__;
				$result[] = new $class($mark['student'], $mark['subject'], $mark['value']);
		}
		return $result;
	}

	public function save():self {
		$db = self::getDB();
		if (self::search($this->student, $this->subject) == [])
			$db -> insert([
				'Mark' => [ 'student' => $this->student,
                            'subject' => $this->subject,
                            'value' => $this->value]
			])->run();

		else
			$db -> update('Mark', ['value' => $this->value] )
				-> where(['Mark'=> ['student' => $this->student,
                                    'subject' => $this->subject]])
				-> run();
		return $this;
	}

	public function __construct(public int $student, public int $subject, public int $value) {
	}
}