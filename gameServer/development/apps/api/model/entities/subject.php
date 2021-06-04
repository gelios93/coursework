<?php

namespace Model\Entities;

class Subject
{
	use \Library\Shared;
	use \Library\Entity;

	public static function search(int $id = 0, string $name = ''):self|null {
		$result = null;
		$db = self::getDB();
        $filters = [];
        foreach (['id', 'name'] as $filter)
            if($$filter)
                $filters[$filter] = $$filter;
        
        $subject = $db -> select(['Subject' => []
			])->where(['Subject' => $filters
			])->one();
		$class = __CLASS__;
		$result = $subject ? new $class($subject['name'], $subject['lecturer'], $subject['id']) : null;
		return $result;
	}

	public function save():self {
		$db = self::getDB();
		if (!$this->id)
			$this->id = $db -> insert([
				'Subject' => [ 'name' => $this->name,
                                'lecturer' => $this->lecturer]
			])->run(true)->storage['inserted'];

		if ($this->_changed)
			$db -> update('Subject', $this->_changed )
				-> where(['Subject'=> ['id' => $this->id]])
				-> run();
		return $this;
	}

	public function __construct(public string $name, public string $lecturer, public int $id = 0) {
	}
}