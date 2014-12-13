package git.sasure.arrway;

import java.util.ArrayList;
import java.util.List;

import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

public class trigonArr extends SquareArrangement
{

	@Override
	protected List<Piece> createArrangement() 
	{
		List<Piece> notNullPieces = new ArrayList<>();
		
		for(int j = GameKit.GameYN - 1,k = 0;j >= GameKit.GameYN / 2;--j,++k)
			for(int i = k;i < GameKit.GameXN - k;++i)
				notNullPieces.add(new Piece(i,j));
		
		for(int j = 0,k = 0;j < GameKit.GameYN / 2;++j,++k)
			for(int i = k;i < GameKit.GameXN - k;++i)
				notNullPieces.add(new Piece(i,j));
			
		return notNullPieces;
	}

}
