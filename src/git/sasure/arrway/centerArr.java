package git.sasure.arrway;

import java.util.ArrayList;
import java.util.List;

import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

/**
 * 其产生的方块排列顺序如：
 * | ****** |
 * | ****** |
 * | ****** |
 * @author Sasure
 * @version 1.0
 */
public class centerArr extends SquareArrangement {

	@Override
	protected List<Piece> createArrangement() 
	{
		List<Piece> notNullPieces = new ArrayList<>();
		
		for(int i = 1;i < GameKit.GameXN - 1;++i)
			for(int j = 1;j < GameKit.GameYN - 1;++j)
				notNullPieces.add(new Piece(i, j));
		
		return notNullPieces;
	}

}
