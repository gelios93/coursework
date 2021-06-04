<?php
namespace Model\Entities;

class Build
{
	use \Library\Shared;
	use \Library\Entity;

	public static function search(string $maket, int $id = 0):self|array|null {
		$result = [];
		$db = self::getDB();
		$services = $db -> select(['Builds' => []]);

		$services->where(['Builds'=> ['maket'=>$maket]]);

		foreach ($services->many() as $service) {
			$class = __CLASS__;
			$result[] = new $class($service['maket'], $service['coords']);
		}
		return $result;
	}

	public function save():self {
		$db = self::getDB();
		if (!$this->id) {
			$insert = [
				'maket' => $this->maket,
				'coords' => $this->coords,
			];
			$this->id = $db -> insert([
				'Builds' => $insert
			])->run(true)->storage['inserted'];
		}

		if ($this->_changed)
			$db -> update('Builds', $this->_changed )
				-> where(['Builds'=> ['id' => $this->id]])
				-> run();
		return $this;
	}


	public function __construct(public string $maket, public string $coords, public int $id = 0) {
	}
}