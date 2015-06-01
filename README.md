# MONSTERFALL
Android Game
```
this.handlerSpawning = new Handler() {
    @Override
    public void handleMessage(Message msg) {
    if(isRunnig) {
        gameLayout.spawnMonster();
        if(level % 2 == 0 && bossSpawned < level / 2){
            bossSpawned++;
            gameLayout.spawnBoss(level);
        }
    }
    }
};
```
