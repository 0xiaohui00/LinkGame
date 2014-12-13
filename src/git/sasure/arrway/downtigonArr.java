package git.sasure.arrway;

import java.util.ArrayList;
import java.util.List;

import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

public class downtigonArr extends SquareArrangement 
{

	@Override
	protected List<Piece> createArrangement() 
	{
		List<Piece> notNullPieces = new ArrayList<>();
		
		for(int j = GameKit.GameYN - 1,k = 0;j >= 0;--j,++k)
			for(int i = k;i < GameKit.GameXN;++i)
				notNullPieces.add(new Piece(i,j));
		
		return notNullPieces;
	}

}
