package git.sasure.sub;

import java.util.ArrayList;
import java.util.List;

import git.sasure.Abs.SquareArrangement;
import git.sasure.Kit.GameKit;
import git.sasure.linkgame.Piece;

public class fullArr extends SquareArrangement {

	@Override
	protected List<Piece> createArrangement() 
	{
		List<Piece> notNullPieces = new ArrayList<>();
		
		for(int i = 0;i < GameKit.GameXN;++i)
			for(int j = 0;j < GameKit.GameYN;++j)
				notNullPieces.add(new Piece(i, j));
		
		return notNullPieces;
	}

}
